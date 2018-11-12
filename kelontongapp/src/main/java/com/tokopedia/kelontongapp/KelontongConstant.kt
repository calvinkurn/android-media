package com.tokopedia.kelontongapp

/**
 * Created by meta on 19/10/18.
 */
object KelontongConstant {

    val VERIFIKASI = "verifikasi"

    object NotificationConstant {

        val CHANNEL_GENERAL = "ANDROID_KELONTONG_GENERAL_CHANNEL_v" + BuildConfig.VERSION_NAME
        val GROUP_GENERAL = "com.tokopedia.kelontong.GENERAL"

        val LOWER_CODE = 1600
        val UPPER_CODE = 1700

        val TARGET_APP = "mitraapp"

        val NOTIFICATION_ID_GENERAL = 1600
    }

    object PreferenceConstant {

        val PREFERENCES_MITRA_APPLICATION = "mitra_application";
        val PREFERENCES_FIREBASE_TOKEN = "mitra_firebase_token";
        val PREFERENCES_FIRST_TIME = "mitra_first_time";
    }
}
