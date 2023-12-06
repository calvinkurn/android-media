package com.tokopedia.payment.utils

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.payment.activity.TopPayActivity.Companion.KEY_FINGERPRINT_DATA

class PaymentFingerprintDataLogger {

    companion object {
        const val TAG = "DEBUG_PAYMENT_POST_DATA"
        const val KEY_IS_TOGGLED_ON = "isToggledOn"
        const val KEY_LOAD_METHOD = "loadMethod"
        const val KEY_LENGTH_ORI = "lengthOri"
        const val KEY_LENGTH_1 = "length1"
        const val KEY_LENGTH_2 = "length2"
        const val KEY_LENGTH_3 = "length3"
        const val KEY_HAS_DIFF_LENGTH = "hasDiffLength"
        const val KEY_TRANSACTION_ID = "transactionId"
        const val KEY_HAS_FINGERPRINT_DATA = "hasFingerprintData"
    }

    var isToggledOn: Boolean = false
    var lengthOri: Int = 0
    var length1: Int = 0
    var length2: Int = 0
    var length3: Int = 0
    var loadMethod: String = ""
    var transactionId: String = ""

    fun sendLog(postData: ByteArray) {
        val differentLength: Boolean = length1 != length2 || length1 != length3
        val mapData = mapOf(
            KEY_IS_TOGGLED_ON to isToggledOn.toString(),
            KEY_LENGTH_ORI to lengthOri.toString(),
            KEY_LENGTH_1 to length1.toString(),
            KEY_LENGTH_2 to length2.toString(),
            KEY_LENGTH_3 to length3.toString(),
            KEY_HAS_DIFF_LENGTH to differentLength.toString(),
            KEY_LOAD_METHOD to loadMethod,
            KEY_TRANSACTION_ID to transactionId,
            KEY_HAS_FINGERPRINT_DATA to String(postData, Charsets.UTF_8).contains(KEY_FINGERPRINT_DATA).toString()
        )

        ServerLogger.log(
            Priority.P2,
            TAG,
            mapData
        )
    }
}
