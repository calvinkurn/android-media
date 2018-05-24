package com.tokopedia.instantloan.ddcollector.bdd;


import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build;

import com.tokopedia.instantloan.ddcollector.BaseCollector;
import com.tokopedia.instantloan.ddcollector.util.AppInfo;
import com.tokopedia.instantloan.ddcollector.util.DeviceInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicDeviceData extends BaseCollector {

    public static final String DD_BASIC_DEVICE_DATA = "bdd";
    public static final String BRAND = "brand";
    public static final String DEVICE_ID = "device_id";
    public static final String DEVICE_SDK_VERSION = "device_sdk_version";
    public static final String DEVICE_SYSTEM_VERSION = "device_system_version";
    public static final String GOOGLE_GAID = "google_gaid";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String MODEL = "model";
    public static final String SYSTEM_LANGUAGE = "system_language";
    public static final String IMEI = "imei";

    private Context mContext;
    private LocationManager mLocationManager;

    public BasicDeviceData(Context context, LocationManager locationManager) {
        this.mContext = context;
        this.mLocationManager = locationManager;
    }

    public String getType() {
        return DD_BASIC_DEVICE_DATA;
    }

    @SuppressLint({"MissingPermission"})
    public Object getData() {
        List<Map<String, String>> phoneInfoList = new ArrayList<>();

        try {
            Map<String, String> basicInfoMap = new HashMap<>();
            basicInfoMap.put(DEVICE_ID, DeviceInfo.getDeviceId(mContext));
            basicInfoMap.put(BRAND, Build.BRAND);
            basicInfoMap.put(GOOGLE_GAID, "Stub");

            try {
                basicInfoMap.put(IMEI, DeviceInfo.getImei(mContext));
            } catch (Exception e) {
                basicInfoMap.put(IMEI, "N/A");
            }

            basicInfoMap.put(IMEI, AppInfo.getDefaultAcceptLanguage(mContext));

            try {
                basicInfoMap.put(LATITUDE, String.valueOf(mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude())); //TODO @lavekush-t impl require
            } catch (NullPointerException npe) {
                basicInfoMap.put(LATITUDE, "0.0"); //TODO @lavekush-t impl require
            }

            try {
                basicInfoMap.put(LONGITUDE, String.valueOf(mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude())); //TODO @lavekush-t impl require
            } catch (NullPointerException npe) {
                basicInfoMap.put(LONGITUDE, "0.0"); //TODO @lavekush-t impl require
            }

            basicInfoMap.put(MODEL, DeviceInfo.getDeviceModelNumber(mContext));
            basicInfoMap.put(DEVICE_SDK_VERSION, String.valueOf(Build.VERSION.SDK_INT));
            basicInfoMap.put(DEVICE_SYSTEM_VERSION, String.valueOf(Build.VERSION.RELEASE));
            basicInfoMap.put(SYSTEM_LANGUAGE, DeviceInfo.getSystemLanguage());
            phoneInfoList.add(basicInfoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return phoneInfoList;
    }
}
