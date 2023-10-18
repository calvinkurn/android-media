package com.tokopedia.sessioncommon.constants

object SessionConstants {
    
    object FirebaseConfig {
        const val CONFIG_ADD_PASSWORD_ENCRYPTION = "android_user_add_password_encryption"
        const val CONFIG_REGISTER_ENCRYPTION = "android_user_register_encryption"
        const val CONFIG_LOGIN_ENCRYPTION = "android_user_login_encryption"
        const val CONFIG_SILENT_VERIFICATION = "android_user_silent_verification"
    }

    enum class CheckPinType(val value: String) {
        USER_ID("userid"),
        PHONE("phone"),
        EMAIL("email")
    }

    enum class GenerateKeyModule(val value: String) {
        PIN_V2("pinv2"),
        PASSWORD("pwd")
    }

    fun cleanPublicKey(key: String): String {
        return key.replace("=", "")
    }
}