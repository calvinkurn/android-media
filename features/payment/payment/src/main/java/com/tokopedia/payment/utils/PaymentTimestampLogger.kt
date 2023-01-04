package com.tokopedia.payment.utils

import android.util.Log
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

class PaymentTimestampLogger {

    var checkoutTimestamp: Long = 0
    var paymentStartLoadTimestamp: Long = 0
    var paymentFinishLoadTimestamp: Long = 0

    companion object {
        const val TAG = "BUYER_FLOW_PAYMENT"
        const val KEY_TYPE = "type"
        const val KEY_CHECKOUT_TIMESTAMP = "checkout_timestamp"
        const val KEY_PAYMENT_START_LOAD_TIMESTAMP = "payment_start_load_timestamp"
        const val KEY_PAYMENT_FINISH_LOAD_TIMESTAMP = "payment_finish_load_timestamp"
        const val VALUE_TYPE = "Checkout - Payment timestamp"
        const val TEN_MINUTES_IN_MILIS = 600000
    }

    fun sendLog() {
        if (checkoutTimestamp == 0L || paymentStartLoadTimestamp == 0L || paymentFinishLoadTimestamp == 0L) return
        val timeDifference = paymentFinishLoadTimestamp - checkoutTimestamp
//        Log.d("BUYER_FLOW_PAYMENT", "$checkoutTimestamp - $paymentStartLoadTimestamp - $paymentFinishLoadTimestamp")
        if (timeDifference >= TEN_MINUTES_IN_MILIS) {
            ServerLogger.log(
                Priority.P2, TAG,
                mapOf(
                    KEY_TYPE to VALUE_TYPE,
                    KEY_CHECKOUT_TIMESTAMP to checkoutTimestamp.toString(),
                    KEY_PAYMENT_START_LOAD_TIMESTAMP to paymentStartLoadTimestamp.toString(),
                    KEY_PAYMENT_FINISH_LOAD_TIMESTAMP to paymentFinishLoadTimestamp.toString()
                )
            )
        }
    }

}
