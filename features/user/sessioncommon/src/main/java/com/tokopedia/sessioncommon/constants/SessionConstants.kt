package com.tokopedia.sessioncommon.constants

object SessionConstants {

    object Rollout {
        const val ROLLOUT_REGISTER_ENCRYPTION = "enf_reg_and"
        const val ROLLOUT_RESET_PASS_ENCRYPTION = "enf_rp_and"
        const val ROLLOUT_ADD_PASS_ENCRYPTION = "enf_ap_and"
        const val ROLLOUT_LOGIN_ENCRYPTION = "enf_cred_and"


        const val ROLLOUT_LOGIN_ENCRYPTION_SELLER = "enf_cred_seller"
        const val ROLLOUT_RESET_PASS_ENCRYPTION_SELLER = "enf_rp_seller"
        const val ROLLOUT_ADD_PASS_ENCRYPTION_SELLER = "enf_ap_seller"
        const val ROLLOUT_REGISTER_ENCRYPTION_SELLER = "enf_reg_seller"
    }

    object FirebaseConfig {
        const val CONFIG_ADD_PASSWORD_ENCRYPTION = "android_user_add_password_encryption"
        const val CONFIG_RESET_PASSWORD_ENCRYPTION = "android_user_reset_password_encryption"
        const val CONFIG_REGISTER_ENCRYPTION = "android_user_register_encryption"
        const val CONFIG_LOGIN_ENCRYPTION = "android_user_login_encryption"
    }
}