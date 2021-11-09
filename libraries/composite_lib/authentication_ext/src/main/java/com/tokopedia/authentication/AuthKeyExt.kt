package com.tokopedia.authentication

class AuthKeyExt {
    companion object {
        private val RAW_RECHARGE_HMAC_API_ORGANIZATION_STAGING = intArrayOf(107, 74, 72, 104, 103, 97, 51, 107, 52, 89)
        private val RAW_RECHARGE_HMAC_API_ORGANIZATION_PROD = intArrayOf(54, 112, 86, 87, 122, 84, 122, 101, 86, 52, 104, 119, 97, 100, 87, 67, 72, 99, 76, 50, 87, 82, 70, 54, 53, 76, 81, 118, 107, 119, 118, 104, 100, 113, 112, 77, 67, 72, 78, 103)

        private val RAW_RECHARGE_HMAC_API_KEY_STAGING = intArrayOf(119, 119, 106, 118, 89, 83, 74, 101, 53, 101, 70, 65, 71, 72, 68, 102, 74, 116, 69, 110, 74, 118, 97, 116, 103, 57, 51, 77, 90, 99)
        private val RAW_RECHARGE_HMAC_API_KEY_PROD = intArrayOf(67, 107, 88, 117, 121, 65, 53, 112, 57, 76, 77, 98, 53, 106, 102, 106, 104, 84, 88, 77, 68, 70, 65, 68, 84, 83, 122, 120, 52, 69, 84, 88, 121, 107, 85, 67, 53, 113, 106, 75)

        @JvmField val RECHARGE_HMAC_API_ORGANIZATION_STAGING = encodeKey(RAW_RECHARGE_HMAC_API_ORGANIZATION_STAGING)
        @JvmField val RECHARGE_HMAC_API_ORGANIZATION_PROD = encodeKey(RAW_RECHARGE_HMAC_API_ORGANIZATION_PROD)

        @JvmField val RECHARGE_HMAC_API_KEY_STAGING = encodeKey(RAW_RECHARGE_HMAC_API_KEY_STAGING)
        @JvmField val RECHARGE_HMAC_API_KEY_PROD = encodeKey(RAW_RECHARGE_HMAC_API_KEY_PROD)

        private fun encodeKey(keys: IntArray): String {
            return keys.joinToString(separator = "") { it.toChar().toString() }
        }
    }
}
