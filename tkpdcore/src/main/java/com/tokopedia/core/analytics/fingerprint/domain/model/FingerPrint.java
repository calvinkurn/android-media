package com.tokopedia.core.analytics.fingerprint.domain.model;

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
    }

    public String getDevice_model() {
        return device_model;
    }

    public void setDevice_model(String device_model) {
        this.device_model = device_model;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getSsid() {
        return ssid;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getDevice_system() {
        return device_system;
    }

    public void setDevice_system(String device_system) {
        this.device_system = device_system;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getScreen_resolution() {
        return screen_resolution;
    }

    public void setScreen_resolution(String screen_resolution) {
        this.screen_resolution = screen_resolution;
    }

    public boolean istablet() {
        return is_tablet;
    }

    public void setIs_tablet(boolean is_tablet) {
        this.is_tablet = is_tablet;
    }

    public String getLocation_latitude() {
        return location_latitude;
    }

    public void setLocation_latitude(String location_latitude) {
        this.location_latitude = location_latitude;
    }

    public void setLocation_longitude(String location_longitude) {
        this.location_longitude = location_longitude;
    }

    public String getLocation_longitude() {
        return location_longitude;
    }

    public boolean isIs_emulator() {
        return is_emulator;
    }

    public void setIs_emulator(boolean is_emulator) {
        this.is_emulator = is_emulator;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getDevice_manufacturer() {
        return device_manufacturer;
    }

    public void setDevice_manufacturer(String device_manufacturer) {
        this.device_manufacturer = device_manufacturer;
    }

    public String getCurrent_os() {
        return current_os;
    }

    public void setCurrent_os(String current_os) {
        this.current_os = current_os;
    }

    public boolean isIs_jailbroken_rooted() {
        return is_jailbroken_rooted;
    }

    public void setIs_jailbroken_rooted(boolean is_jailbroken_rooted) {
        this.is_jailbroken_rooted = is_jailbroken_rooted;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getUser_agent() {
        return user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
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

        public FingerPrintBuilder() {

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

        public FingerPrint build() {
            return new FingerPrint(this);
        }

    }

}
