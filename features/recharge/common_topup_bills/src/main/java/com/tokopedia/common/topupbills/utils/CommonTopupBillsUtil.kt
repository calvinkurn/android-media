package com.tokopedia.common.topupbills.utils

import android.content.Context
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

class CommonTopupBillsUtil {

    companion object {
        const val REMOTE_CONFIG_MAINAPP_DIGITAL_FAVORITE_NUMBER = "android_customer_enable_digital_favorite_number"

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
                var phoneNumberWithPrefix = validatePrefixClientNumber(phoneNumber)
                if (!phoneNumberWithPrefix.startsWith("0")) {
                    phoneNumberWithPrefix = "0$phoneNumber"
                }
                return phoneNumberWithPrefix
            }
            return ""
        }

        private fun validatePrefixClientNumber(phoneNumber: String): String {
            var phoneNumber = phoneNumber
            if (phoneNumber.startsWith("62")) {
                phoneNumber = phoneNumber.replaceFirst("62".toRegex(), "0")
            }
            if (phoneNumber.startsWith("+62")) {
                phoneNumber = phoneNumber.replace("+62", "0")
            }
            phoneNumber = phoneNumber.replace(".", "")

            return phoneNumber.replace("[^0-9]+".toRegex(), "")
        }

    }
}