package com.someshswami9.bluetoothapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView deviceListView;
    TextView textView;
    Button searchButton;
    ArrayList<String> bluetoothDevices = new ArrayList<>();
    ArrayList<String> addresses = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    BluetoothAdapter bluetoothAdapter;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("Blutooth Info",action);

            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                textView.setText("");
                searchButton.setEnabled(true);
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device =intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceAddress = device.getAddress();
                String rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE));
                if(!addresses.contains(deviceAddress)) {
                    addresses.add(deviceAddress);
                    String deviceAddressAndRSSI = "";
                    if (deviceName == null || deviceName == "") {
                        deviceAddressAndRSSI = deviceAddress + " - RSSI" +rssi+"dBm";
                    } else {
                        deviceAddressAndRSSI = deviceAddress + " - RSSI" +rssi+"dBm";
                    }
                    bluetoothDevices.add(deviceAddressAndRSSI);
                    arrayAdapter.notifyDataSetChanged();

                }


                Log.i("BLUETOOTH INFO", "Name: " + deviceName + "Address: " + deviceAddress + "RSSI: "+ rssi);

            }
        }
    };

    public void searchButton (View view){
        textView.setText("Searching...");
        searchButton.setEnabled(false);
        bluetoothDevices.clear();
        addresses.clear();
        bluetoothAdapter.startDiscovery();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deviceListView =  (ListView) findViewById(R.id.deviceListView);
        textView = (TextView) findViewById(R.id.textView);
        searchButton = (Button) findViewById(R.id.searchButton);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,bluetoothDevices);
        deviceListView.setAdapter(arrayAdapter);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver,intentFilter);



    }
}