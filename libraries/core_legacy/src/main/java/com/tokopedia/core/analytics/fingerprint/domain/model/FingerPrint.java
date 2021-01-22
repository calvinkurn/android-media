package com.tokopedia.core.analytics.fingerprint.domain.model;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Herdi_WORK on 20.06.17.
 */

public class FingerPrint {

    @SerializedName("device_model")
    @Expose
    private String device_model;
    @SerializedName("device_system")
    @Expose
    private String device_system;
    @SerializedName("current_os")
    @Expose
    private String current_os;
    @SerializedName("device_manufacturer")
    @Expose
    private String device_manufacturer;
    @SerializedName("device_name")
    @Expose
    private String device_name;
    @SerializedName("is_jailbroken_rooted")
    @Expose
    private boolean is_jailbroken_rooted;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("user_agent")
    @Expose
    private String user_agent;
    @SerializedName("is_emulator")
    @Expose
    private boolean is_emulator;
    @SerializedName("is_tablet")
    @Expose
    private boolean is_tablet;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("carrier")
    @Expose
    private String carrier;
    @SerializedName("ssid")
    @Expose
    private String ssid;
    @SerializedName("screen_resolution")
    @Expose
    private String screen_resolution;
    @SerializedName("location_latitude")
    @Expose
    private String location_latitude;
    @SerializedName("location_longitude")
    @Expose
    private String location_longitude;
    @SerializedName("is_nakama")
    @Expose
    private String is_nakama;
    @SerializedName("unique_id")
    @Expose
    private String unique_id;
    @SerializedName("deviceMemoryClassCapacity")
    @Expose
    private String deviceMemoryClassCapacity;
    @SerializedName("availableProcessor")
    @Expose
    private String availableProcessor;
    @SerializedName("deviceDpi")
    @Expose
    private String deviceDpi;
    @SerializedName("packageName")
    @Expose
    private String packageName;
    @SerializedName("androidId")
    @Expose
    private String androidId;
    @SerializedName("isx86")
    @Expose
    private boolean isx86;
    @SerializedName("pid")
    @Expose
    private String pid;
    @SerializedName("uuid")
    @Expose
    private String uuid;

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
        uuid = fingerPrintBuilder.uuid;
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
        private String uuid;

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

        public FingerPrintBuilder uuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public FingerPrint build() {
            return new FingerPrint(this);
        }

    }

}
