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

        val KEY_WSV4_NEW = encodeKey(RAW_KEY_WSV4)
        val KEY_WSV4 = "web_service_v4"
        val KEY_MOJITO = "mojito_api_v1"
        val KEY_KEROPPI = "Keroppi"
        val TOKO_CASH_HMAC = "CPAnAGpC3NIg7ZSj"
        var KEY_CREDIT_CARD_VAULT = encodeKey(RAW_SCROOGE_KEY)
        var ZEUS_WHITELIST = encodeKey(RAW_ZEUS_KEY)
        var KEY_NOTP = encodeKey(RAW_NOTP_KEY)
        var KEY_BRANCHIO = encodeKey(RAW_BRANCHIO_KEY_ID)
        var ALIYUN_SECRET_KEY = encodeKey(RAW_ALIYUN_SECRET_KEY)
        var ALIYUN_ACCESS_KEY_ID = encodeKey(RAW_ALIYUN_ACCESS_KEY_ID)
        var INDI_API_KEY = encodeKey(RAW_INDI_API_KEY)
        var API_KEY_INSTANT_DEBIT_BCA = encodeKey(RAW_API_KEY_INSTANT_DEBIT_BCA)
        var API_SEED_INSTANT_DEBIT_BCA = encodeKey(RAW_API_SECRET_INSTANT_DEBIT_BCA)
        var INSTANT_DEBIT_BCA_MERCHANT_ID = encodeKey(RAW_INSTANT_DEBIT_BCA_MERCHANT_ID)
        var INSTANT_DEBIT_BCA_BANK_CODE = encodeKey(RAW_INSTANT_DEBIT_BCA_BANK_CODE)
        var INSTANT_DEBIT_BCA_MERCHANT_CODE = encodeKey(RAW_INSTANT_DEBIT_BCA_MERCHANT_CODE)
        var INSTANT_DEBIT_BCA_PROFILE_CODE = encodeKey(RAW_INSTANT_DEBIT_BCA_PROFILE_CODE)

        private fun encodeKey(keys: IntArray): String {
            return keys.joinToString(separator = "") { it.toChar().toString() }
        }
    }
}
