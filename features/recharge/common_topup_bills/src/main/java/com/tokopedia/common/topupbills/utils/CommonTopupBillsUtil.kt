package com.tokopedia.common.topupbills.utils

import android.content.Context
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

class CommonTopupBillsUtil {

    companion object {
        const val NUMBER_CODE = "62"
        const val FORMATTED_NUMBER_CODE = "+62"
        const val NORMALIZED_NUMBER_PREFIX = "0"
        const val NUMBER_REGEX = "[^0-9]+"
        const val SYMBOL_REGEX = "[+-.:;]"

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
            phoneNumber = phoneNumber.replace(SYMBOL_REGEX.toRegex(), "")

            return phoneNumber.replace(NUMBER_REGEX.toRegex(), "")
        }

        fun buildRedirectAppLinkToCheckout(
            productId: String,
            clientNumber: String,
            categoryId: String
        ): String {
            return """
                tokopedia://digital/cart?product_id=$productId&client_number=$clientNumber
                &category_id=$categoryId&operator_id=&idem_potency_key=&instant_checkout=&slug=
            """.filterNot { it.isWhitespace() }
        }
    }
}
