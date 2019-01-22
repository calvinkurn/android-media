package com.tokopedia.instantloan.constant

object DeviceDataKeys {

    object Sms {
        val PHONE = "phone"
        val CONTENT = "content"
        val TYPE = "type"
        val TIME = "time"
    }

    object Contact {
        val NAME = "name"
        val PHONE = "phone"
        val TIME = "time"
        val TIMES = "times"
        val LAST_TIME = "last_time"
    }

    object Call {
        val PHONE = "phone"
        val TYPE = "type"
        val DURATION = "duration"
        val TIME = "time"
    }

    object Account {
        val NAME = "name"
        val TYPE = "type"
    }

    object App {
        val APP_NAME = "app_name"
        val PACKAGE_NAME = "package_name"
        val INSTALL_TIME = "install_time"
        val UPDATE_TIME = "update_time"
        val APP_TYPE = "app_type"
    }

    object Common {

        val BRAND = "brand"
        val DEVICE_ID = "device_id"
        val DEVICE_SDK_VERSION = "device_sdk_version"
        val DEVICE_SYSTEM_VERSION = "device_system_version"
        val GOOGLE_GAID = "google_gaid"
        val LATITUDE = "latitude"
        val LONGITUDE = "longitude"
        val MODEL = "model"
        val SYSTEM_LANGUAGE = "system_language"
        val IMEI = "imei"
        val SMS = "sms"
        val CONTACT = "contact"
        val CALL = "call_logs"
        val ACCOUNTS = "accounts"
        val APPS = "apps"
    }
}
