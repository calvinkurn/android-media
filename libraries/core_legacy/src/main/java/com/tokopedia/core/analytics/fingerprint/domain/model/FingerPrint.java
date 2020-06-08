package com.tokopedia.core.analytics.fingerprint.domain.model;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Herdi_WORK on 20.06.17.
 */

public class FingerPrint {

    private String device_model;
    private String device_system;
    private String current_os;
    private String device_manufacturer;
    private String device_name;
    private boolean is_jailbroken_rooted;
    private String timezone;
    private String user_agent;
    private boolean is_emulator;
    private boolean is_tablet;
    private String language;
    private String carrier;
    private String ssid;
    private String screen_resolution;
    private String location_latitude;
    private String location_longitude;
    private String is_nakama;
    private String unique_id;
    private String deviceMemoryClassCapacity;
    private String availableProcessor;
    private String deviceDpi;
    private String packageName;
    private String androidId;
    private boolean isx86;
    private String pid;

    private FingerPrint(FingerPrintBuilder fingerPrintBuilder) {
        device_model = fingerPrintBuilder.deviceModel;
        device_name = fingerPrintBuilder.deviceName;
        device_manufacturer = fingerPrintBuilder.deviceManufacturer;
        device_system = fingerPrintBuilder.deviceSystem;
        current_os = fingerPrintBuilder.currentOS;
        is_jailbroken_rooted = fingerPrintBuilder.isJailBreak;
        timezone = fingerPrintBuilder.timezone;
        user_agent = fingerPrintBuilder.userAgent;
        is_emulator = fingerPrintBuilder.isEmulator;
        is_tablet = fingerPrintBuilder.istablet;
        location_latitude = fingerPrintBuilder.lat;
        location_longitude = fingerPrintBuilder.lng;
        screen_resolution = fingerPrintBuilder.screenResolution;
        language = fingerPrintBuilder.language;
        ssid = fingerPrintBuilder.ssid;
        carrier = fingerPrintBuilder.carrier;
        is_nakama = fingerPrintBuilder.is_nakama;
        unique_id = fingerPrintBuilder.unique_id;
        deviceMemoryClassCapacity = fingerPrintBuilder.deviceMemoryClassCapacity;
        availableProcessor = fingerPrintBuilder.availableProcessor;
        deviceDpi = fingerPrintBuilder.deviceDpi;
        androidId = fingerPrintBuilder.androidId;
        isx86 = fingerPrintBuilder.isx86;
        packageName = fingerPrintBuilder.packageName;
        if (TextUtils.isEmpty(fingerPrintBuilder.imei)) {
            pid = "";
        } else {
            pid = fingerPrintBuilder.imei;
        }
    }

    public static class FingerPrintBuilder {
        private String deviceName;
        private String deviceModel;
        private String deviceManufacturer;
        private String deviceSystem;
        private String currentOS;
        private boolean isJailBreak;
        private String timezone;
        private String userAgent;
        private boolean isEmulator;
        private boolean istablet;
        private String lat;
        private String lng;
        private String screenResolution;
        private String language;
        private String ssid;
        private String carrier;
        private String is_nakama;
        private String unique_id;
        private String deviceMemoryClassCapacity;
        private String availableProcessor;
        private String deviceDpi;
        private String androidId;
        private boolean isx86;
        private String packageName;
        private String imei;

        public FingerPrintBuilder() {

        }

        public FingerPrintBuilder uniqueId(String unique_id){
            this.unique_id = unique_id;
            return this;
        }

        public FingerPrintBuilder isNakama(String isNakama){
            this.is_nakama = isNakama;
            return this;
        }

        public FingerPrintBuilder system(String deviceSystem) {
            this.deviceSystem = deviceSystem;
            return this;
        }

        public FingerPrintBuilder carrier(String carrier) {
            this.carrier = carrier;
            return this;
        }

        public FingerPrintBuilder model(String model) {
            this.deviceModel = model;
            return this;
        }

        public FingerPrintBuilder ssid(String ssid) {
            this.ssid = ssid;
            return this;
        }

        public FingerPrintBuilder language(String language) {
            this.language = language;
            return this;
        }

        public FingerPrintBuilder screenReso(String screenResolution) {
            this.screenResolution = screenResolution;
            return this;
        }

        public FingerPrintBuilder deviceLng(String lng) {
            this.lng = lng;
            return this;
        }

        public FingerPrintBuilder deviceLat(String lat) {
            this.lat = lat;
            return this;
        }

        public FingerPrintBuilder tablet(boolean istablet) {
            this.istablet = istablet;
            return this;
        }

        public FingerPrintBuilder emulator(boolean isEmulator) {
            this.isEmulator = isEmulator;
            return this;
        }

        public FingerPrintBuilder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public FingerPrintBuilder timezone(String timezone) {
            this.timezone = timezone;
            return this;
        }

        public FingerPrintBuilder deviceName(String deviceName) {
            this.deviceName = deviceName;
            return this;
        }

        public FingerPrintBuilder deviceManufacturer(String deviceMfn) {
            this.deviceManufacturer = deviceMfn;
            return this;
        }

        public FingerPrintBuilder currentOS(String currentOS) {
            this.currentOS = currentOS;
            return this;
        }

        public FingerPrintBuilder jailbreak(boolean isJailBreak) {
            this.isJailBreak = isJailBreak;
            return this;
        }

        public FingerPrintBuilder deviceMemoryClassCapacity(String deviceMemoryClassCapacity) {
            this.deviceMemoryClassCapacity = deviceMemoryClassCapacity;
            return this;
        }

        public FingerPrintBuilder availableProcessor(String availableProcessor) {
            this.availableProcessor = availableProcessor;
            return this;
        }

        public FingerPrintBuilder deviceDpi(String deviceDpi) {
            this.deviceDpi = deviceDpi;
            return this;
        }

        public FingerPrintBuilder androidId(String androidId) {
            this.androidId = androidId;
            return this;
        }

        public FingerPrintBuilder isx86(boolean isx86) {
            this.isx86 = isx86;
            return this;
        }

        public FingerPrintBuilder packageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public FingerPrintBuilder imei(String imei) {
            this.imei = imei;
            return this;
        }

        public FingerPrint build() {
            return new FingerPrint(this);
        }

    }

}
