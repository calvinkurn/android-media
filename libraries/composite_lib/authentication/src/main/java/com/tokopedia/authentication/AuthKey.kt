package com.tokopedia.authentication

class AuthKey {
    companion object {
        private val RAW_ALIYUN_ACCESS_KEY_ID = intArrayOf(76, 84, 65, 73, 85, 101, 69, 87, 83, 118, 105, 97, 49, 75, 107, 87)
        private val RAW_ALIYUN_SECRET_KEY = intArrayOf(101, 74, 76, 86, 51, 80, 74, 67, 67, 69, 110, 55, 115, 113, 102, 53, 118, 86, 114, 73, 122, 97, 69, 83, 84, 102, 115, 78, 100, 109)
        private val RAW_API_KEY_INSTANT_DEBIT_BCA = intArrayOf(97, 99, 97, 53, 98, 52, 50, 55, 45, 99, 102, 52, 97, 45, 52, 102, 49, 52, 45, 98, 51, 101, 51, 45, 101, 54, 56, 53, 50, 101, 55, 98, 56, 101, 49, 50)
        private val RAW_API_SECRET_INSTANT_DEBIT_BCA = intArrayOf(101, 102, 101, 48, 51, 98, 52, 100, 45, 101, 102, 49, 102, 45, 52, 98, 98, 98, 45, 57, 97, 99, 100, 45, 53, 53, 100, 101, 50, 48, 49, 54, 56, 102, 49, 99)
        private val RAW_BRANCHIO_KEY_ID = intArrayOf(107, 101, 121, 95, 108, 105, 118, 101, 95, 97, 98, 104, 72, 103, 73, 104, 49, 68, 81, 105, 117, 80, 120, 100, 66, 78, 103, 57, 69, 88, 101, 112, 100, 68, 117, 103, 119, 119, 107, 72, 114)
        private val RAW_INDI_API_KEY = intArrayOf(69, 69, 82, 73, 120, 119, 88, 70, 54, 52, 52, 99, 49, 69, 49, 84, 111, 53, 112, 117, 76, 56, 120, 78, 80, 53, 80, 118, 76, 72, 83, 118, 50, 52, 48, 80, 121, 78, 89, 102)
        private val RAW_INSTANT_DEBIT_BCA_BANK_CODE = intArrayOf(66, 67, 65)
        private val RAW_INSTANT_DEBIT_BCA_MERCHANT_CODE = intArrayOf(116, 111, 107, 111, 112, 101, 100, 105, 97)
        private val RAW_INSTANT_DEBIT_BCA_MERCHANT_ID = intArrayOf(54, 49, 48, 49, 55)
        private val RAW_INSTANT_DEBIT_BCA_PROFILE_CODE = intArrayOf(84, 75, 80, 68, 95, 68, 69, 70, 65, 85, 76, 84)
        private val RAW_KEY_WSV4 = intArrayOf(65, 107, 102, 105, 101, 119, 56, 51, 52, 50, 57, 56, 80, 79, 105, 110, 118)
        private val RAW_NOTP_KEY = intArrayOf(110, 117, 108, 97, 121, 117, 107, 97, 119, 111, 106, 117)
        private val RAW_SCROOGE_KEY = intArrayOf(49, 50, 69, 56, 77, 105, 69, 55, 89, 69, 54, 86, 122, 115, 69, 80, 66, 80, 101, 77)
        private val RAW_ZEUS_KEY = intArrayOf(102, 100, 100, 98, 100, 56, 49, 101, 101, 52, 49, 49, 54, 98, 56, 99, 98, 55, 97, 52, 48, 56, 100, 55, 102, 98, 102, 98, 57, 99, 49, 55)
        private val RAW_BRIZZI_CLIENT_SECRET = intArrayOf(82, 82, 49, 68, 74, 56, 81, 90, 115, 89, 85, 88, 84, 90, 80, 98)
        private val RAW_BRIZZI_CLIENT_ID = intArrayOf(116, 88, 110, 65, 54, 71, 122, 113, 57, 74, 52, 51, 57, 71, 73, 51, 106, 74, 57, 66, 106, 99, 66, 71, 48, 53, 116, 109, 113, 73, 81, 97)
        private val RAW_SAFETYNET_KEY_TRADE_IN = intArrayOf(65, 73, 122, 97, 83, 121, 66, 117, 71, 83, 108, 109, 49, 68, 100, 85, 56, 79, 82, 99, 66, 76, 49, 119, 86, 122, 78, 53, 102, 69, 110, 67, 55, 71, 104, 87, 111, 68, 111)
        private val RAW_RECHARGE_HMAC_API_ORGANIZATION_STAGING = intArrayOf(107, 74, 72, 104, 103, 97, 51, 107, 52, 89)
        private val RAW_RECHARGE_HMAC_API_ORGANIZATION_PROD = intArrayOf(54, 112, 86, 87, 122, 84, 122, 101, 86, 52, 104, 119, 97, 100, 87, 67, 72, 99, 76, 50, 87, 82, 70, 54, 53, 76, 81, 118, 107, 119, 118, 104, 100, 113, 112, 77, 67, 72, 78, 103)
        private val RAW_RECHARGE_HMAC_API_KEY_STAGING = intArrayOf(119, 119, 106, 118, 89, 83, 74, 101, 53, 101, 70, 65, 71, 72, 68, 102, 74, 116, 69, 110, 74, 118, 97, 116, 103, 57, 51, 77, 90, 99)
        private val RAW_RECHARGE_HMAC_API_KEY_PROD = intArrayOf(67, 107, 88, 117, 121, 65, 53, 112, 57, 76, 77, 98, 53, 106, 102, 106, 104, 84, 88, 77, 68, 70, 65, 68, 84, 83, 122, 120, 52, 69, 84, 88, 121, 107, 85, 67, 53, 113, 106, 75)`

        @JvmField val KEY_WSV4_NEW = encodeKey(RAW_KEY_WSV4)
        @JvmField val KEY_WSV4 = "web_service_v4"
        @JvmField val KEY_MOJITO = "mojito_api_v1"
        @JvmField val KEY_KEROPPI = "Keroppi"
        @JvmField val TOKO_CASH_HMAC = "CPAnAGpC3NIg7ZSj"
        @JvmField val KEY_CREDIT_CARD_VAULT = encodeKey(RAW_SCROOGE_KEY)
        @JvmField val ZEUS_WHITELIST = encodeKey(RAW_ZEUS_KEY)
        @JvmField val KEY_NOTP = encodeKey(RAW_NOTP_KEY)
        @JvmField val KEY_BRANCHIO = encodeKey(RAW_BRANCHIO_KEY_ID)
        @JvmField val ALIYUN_SECRET_KEY = encodeKey(RAW_ALIYUN_SECRET_KEY)
        @JvmField val ALIYUN_ACCESS_KEY_ID = encodeKey(RAW_ALIYUN_ACCESS_KEY_ID)
        @JvmField val INDI_API_KEY = encodeKey(RAW_INDI_API_KEY)
        @JvmField val API_KEY_INSTANT_DEBIT_BCA = encodeKey(RAW_API_KEY_INSTANT_DEBIT_BCA)
        @JvmField val API_SEED_INSTANT_DEBIT_BCA = encodeKey(RAW_API_SECRET_INSTANT_DEBIT_BCA)
        @JvmField val INSTANT_DEBIT_BCA_MERCHANT_ID = encodeKey(RAW_INSTANT_DEBIT_BCA_MERCHANT_ID)
        @JvmField val INSTANT_DEBIT_BCA_BANK_CODE = encodeKey(RAW_INSTANT_DEBIT_BCA_BANK_CODE)
        @JvmField val INSTANT_DEBIT_BCA_MERCHANT_CODE = encodeKey(RAW_INSTANT_DEBIT_BCA_MERCHANT_CODE)
        @JvmField val INSTANT_DEBIT_BCA_PROFILE_CODE = encodeKey(RAW_INSTANT_DEBIT_BCA_PROFILE_CODE)
        @JvmField val BRIZZI_CLIENT_SECRET = encodeKey(RAW_BRIZZI_CLIENT_SECRET)
        @JvmField val BRIZZI_CLIENT_ID = encodeKey(RAW_BRIZZI_CLIENT_ID)
        @JvmField val SAFETYNET_KEY_TRADE_IN = encodeKey(RAW_SAFETYNET_KEY_TRADE_IN)
        @JvmField val RECHARGE_HMAC_API_ORGANIZATION_STAGING = encodeKey(RAW_RECHARGE_HMAC_API_ORGANIZATION_STAGING)
        @JvmField val RECHARGE_HMAC_API_ORGANIZATION_PROD = encodeKey(RAW_RECHARGE_HMAC_API_ORGANIZATION_PROD)
        @JvmField val RECHARGE_HMAC_API_KEY_STAGING = encodeKey(RAW_RECHARGE_HMAC_API_KEY_STAGING)
        @JvmField val RECHARGE_HMAC_API_KEY_PROD = encodeKey(RAW_RECHARGE_HMAC_API_KEY_PROD)

        private fun encodeKey(keys: IntArray): String {
            return keys.joinToString(separator = "") { it.toChar().toString() }
        }
    }
}
