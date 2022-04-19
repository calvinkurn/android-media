package com.tokopedia.sessioncommon.constants

object SessionConstants {

    /* Please add session rollout key here */
    object Rollout {

    }

    object FirebaseConfig {
        const val CONFIG_ADD_PASSWORD_ENCRYPTION = "android_user_add_password_encryption"
        const val CONFIG_RESET_PASSWORD_ENCRYPTION = "android_user_reset_password_encryption"
        const val CONFIG_REGISTER_ENCRYPTION = "android_user_register_encryption"
        const val CONFIG_LOGIN_ENCRYPTION = "android_user_login_encryption"
        const val CONFIG_SILENT_VERIFICATION = "android_user_silent_verification"
    }
}