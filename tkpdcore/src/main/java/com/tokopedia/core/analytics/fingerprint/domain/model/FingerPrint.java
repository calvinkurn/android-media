package com.tokopedia.core.analytics.fingerprint.domain.model;

/**
 * Created by Herdi_WORK on 20.06.17.
 */

public class FingerPrint {

    private String deviceID;
    private String deviceName;
    private String deviceManufacturer;
    private String currentOS;
    private boolean isJailBreak;
    private String timezone;
    private String userAgent;
    private boolean isEmulator;
    private boolean istablet;
    private double lat;
    private double lng;
    private String buildNumber;
    private String ipAddress;
    private String screenResolution;
    private String installedKeyboard;
    private String language;

    private FingerPrint(FingerPrintBuilder fingerPrintBuilder){
        deviceID = fingerPrintBuilder.deviceID;
        deviceName = fingerPrintBuilder.deviceName;
        deviceManufacturer = fingerPrintBuilder.deviceManufacturer;
        currentOS = fingerPrintBuilder.currentOS;
        isJailBreak = fingerPrintBuilder.isJailBreak;
        timezone = fingerPrintBuilder.timezone;
        userAgent = fingerPrintBuilder.userAgent;
        isEmulator = fingerPrintBuilder.isEmulator;
        istablet = fingerPrintBuilder.istablet;
        lat = fingerPrintBuilder.lat;
        lng = fingerPrintBuilder.lng;
        buildNumber = fingerPrintBuilder.buildNumber;
        ipAddress = fingerPrintBuilder.ipAddress;
        screenResolution = fingerPrintBuilder.screenResolution;
        installedKeyboard = fingerPrintBuilder.installedKeyboard;
        language = fingerPrintBuilder.language;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getInstalledKeyboard() {
        return installedKeyboard;
    }

    public void setInstalledKeyboard(String installedKeyboard) {
        this.installedKeyboard = installedKeyboard;
    }

    public String getScreenResolution() {
        return screenResolution;
    }

    public void setScreenResolution(String screenResolution) {
        this.screenResolution = screenResolution;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public boolean istablet() {
        return istablet;
    }

    public void setIstablet(boolean istablet) {
        this.istablet = istablet;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLng() {
        return lng;
    }

    public boolean isEmulator() {
        return isEmulator;
    }

    public void setEmulator(boolean emulator) {
        isEmulator = emulator;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceManufacturer() {
        return deviceManufacturer;
    }

    public void setDeviceManufacturer(String deviceManufacturer) {
        this.deviceManufacturer = deviceManufacturer;
    }

    public String getCurrentOS() {
        return currentOS;
    }

    public void setCurrentOS(String currentOS) {
        this.currentOS = currentOS;
    }

    public boolean isJailBreak() {
        return isJailBreak;
    }

    public void setJailBreak(boolean jailBreak) {
        isJailBreak = jailBreak;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public static class FingerPrintBuilder {
        private String deviceID;
        private String deviceName;
        private String deviceManufacturer;
        private String currentOS;
        private boolean isJailBreak;
        private String timezone;
        private String userAgent;
        private boolean isEmulator;
        private boolean istablet;
        private double lat;
        private double lng;
        private String buildNumber;
        private String ipAddress;
        private String screenResolution;
        private String installedKeyboard;
        private String language;

        public FingerPrintBuilder() {

        }

        public FingerPrintBuilder language(String language) {
            this.language = language;
            return this;
        }

        public FingerPrintBuilder installedKeyboard(String installedKeyboard) {
            this.installedKeyboard = installedKeyboard;
            return this;
        }

        public FingerPrintBuilder screenReso(String screenResolution) {
            this.screenResolution = screenResolution;
            return this;
        }

        public FingerPrintBuilder ipAddres(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public FingerPrintBuilder buildNumber(String buildNumber) {
            this.buildNumber = buildNumber;
            return this;
        }

        public FingerPrintBuilder deviceLng(double lng) {
            this.lng = lng;
            return this;
        }

        public FingerPrintBuilder deviceLat(double lat) {
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

        public FingerPrintBuilder deviceID(String deviceID) {
            this.deviceID = deviceID;
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

        public FingerPrint build() {
            return new FingerPrint(this);
        }

    }

}
