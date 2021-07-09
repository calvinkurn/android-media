package com.tokopedia.authentication

class AuthKeyExt {
    companion object {
        private val RAW_RECHARGE_HMAC_API_ORGANIZATION_STAGING = intArrayOf(107, 74, 72, 104, 103, 97, 51, 107, 52, 89)
        private val RAW_RECHARGE_HMAC_API_ORGANIZATION_PROD = intArrayOf(54, 112, 86, 87, 122, 84, 122, 101, 86, 52, 104, 119, 97, 100, 87, 67, 72, 99, 76, 50, 87, 82, 70, 54, 53, 76, 81, 118, 107, 119, 118, 104, 100, 113, 112, 77, 67, 72, 78, 103)

        @JvmField val RECHARGE_HMAC_API_ORGANIZATION_STAGING = encodeKey(RAW_RECHARGE_HMAC_API_ORGANIZATION_STAGING)
        @JvmField val RECHARGE_HMAC_API_ORGANIZATION_PROD = encodeKey(RAW_RECHARGE_HMAC_API_ORGANIZATION_PROD)

        private fun encodeKey(keys: IntArray): String {
            return keys.joinToString(separator = "") { it.toChar().toString() }
        }
    }
}
