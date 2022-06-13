package com.tokopedia.common.topupbills.utils

import android.content.Context
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

class CommonTopupBillsUtil {

    companion object {
        const val REMOTE_CONFIG_MAINAPP_DIGITAL_FAVORITE_NUMBER = "android_customer_enable_digital_favorite_number"

        const val NUMBER_CODE = "62"
        const val FORMATTED_NUMBER_CODE = "+62"
        const val NORMALIZED_NUMBER_PREFIX = "0"
        const val NUMBER_REGEX = "[^0-9]+"

        fun isSeamlessFavoriteNumber(context: Context): Boolean {
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            return remoteConfig.getBoolean(REMOTE_CONFIG_MAINAPP_DIGITAL_FAVORITE_NUMBER, true)
        }

        fun getApplinkFavoriteNumber(context: Context): String {
            return if (isSeamlessFavoriteNumber(context))
                ApplinkConsInternalDigital.FAVORITE_NUMBER else ApplinkConsInternalDigital.SEARCH_NUMBER
        }

        fun formatPrefixClientNumber(phoneNumber: String?): String {
            phoneNumber?.run {
                if ("".equals(phoneNumber.trim { it <= ' ' }, ignoreCase = true)) {
                    return phoneNumber
                }
                return validatePrefixClientNumber(phoneNumber)
            }
            return ""
        }

        private fun validatePrefixClientNumber(phoneNumber: String): String {
            var phoneNumber = phoneNumber
            if (phoneNumber.startsWith(NUMBER_CODE)) {
                phoneNumber = phoneNumber.replaceFirst(NUMBER_CODE.toRegex(), NORMALIZED_NUMBER_PREFIX)
            }
            if (phoneNumber.startsWith(FORMATTED_NUMBER_CODE)) {
                phoneNumber = phoneNumber.replace(FORMATTED_NUMBER_CODE, NORMALIZED_NUMBER_PREFIX)
            }
            phoneNumber = phoneNumber.replace(".", "")

            return phoneNumber.replace(NUMBER_REGEX.toRegex(), "")
        }
    }
}