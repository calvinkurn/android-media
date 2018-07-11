package com.tokopedia.instantloan.constant;

/**
 * Created by lavekush on 28/03/18.
 */

public interface DeviceDataKeys {

    String BRAND = "brand";
    String DEVICE_ID = "device_id";
    String DEVICE_SDK_VERSION = "device_sdk_version";
    String DEVICE_SYSTEM_VERSION = "device_system_version";
    String GOOGLE_GAID = "google_gaid";
    String LATITUDE = "latitude";
    String LONGITUDE = "longitude";
    String MODEL = "model";
    String SYSTEM_LANGUAGE = "system_language";
    String IMEI = "imei";
    String SMS = "sms";
    String CONTACT = "contact";
    String CALL = "call_logs";
    String ACCOUNTS = "accounts";
    String APPS = "apps";

    interface Sms {
        String PHONE = "phone";
        String CONTENT = "content";
        String TYPE = "type";
        String TIME = "time";
    }

    interface Contact {
        String NAME = "name";
        String PHONE = "phone";
        String TIME = "time";
        String TIMES = "times";
        String LAST_TIME = "last_time";
    }

    interface Call {
        String PHONE = "phone";
        String TYPE = "type";
        String DURATION = "duration";
        String TIME = "time";
    }

    interface Account {
        String NAME = "name";
        String TYPE = "type";
    }

    interface App {
        String APP_NAME = "app_name";
        String PACKAGE_NAME = "package_name";
        String INSTALL_TIME = "install_time";
        String UPDATE_TIME = "update_time";
        String APP_TYPE = "app_type";
    }
}
