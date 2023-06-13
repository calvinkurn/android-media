package com.tokopedia.payment.utils

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import okhttp3.internal.toLongOrDefault

class PaymentTimestampLogger(val remoteConfig: RemoteConfig) {

    var checkoutTimestamp: Long = 0
    var paymentStartLoadTimestamp: Long = 0
    var paymentFinishLoadTimestamp: Long = 0

    companion object {
        const val TAG = "DEBUG_CHECKOUT_GENERAL_PAYMENT"
        const val KEY_TYPE = "type"
        const val KEY_CHECKOUT_TIMESTAMP = "checkout_timestamp"
        const val KEY_PAYMENT_START_LOAD_TIMESTAMP = "payment_start_load_timestamp"
        const val KEY_TOTAL_TIME_TO_OPEN_PAYMENT_PAGE = "time_to_open_payment_page"
        const val KEY_PAYMENT_FINISH_LOAD_TIMESTAMP = "payment_finish_load_timestamp"
        const val KEY_TOTAL_TIME_TO_LOAD_PAYMENT_PAGE = "time_to_load_payment_page"
        const val KEY_TOTAL_TIME = "time_total"
        const val VALUE_TYPE = "overlong_payment_load"
        const val FIVE_MINUTES_IN_MILIS = 300000L

        const val MILIS_DIVIDER = 1000
        const val SECOND_DIVIDER = 60
        const val MINUTE_DIVIDER = 60
        const val HOUR_DIVIDER = 24

        const val TIME_FORMAT = "%02d:%02d:%02d.%d"
    }

    fun sendLog() {
        if (isSendLog()) {
            val data = mapOf(
                KEY_TYPE to VALUE_TYPE,
                KEY_CHECKOUT_TIMESTAMP to checkoutTimestamp.toString(),
                KEY_PAYMENT_START_LOAD_TIMESTAMP to paymentStartLoadTimestamp.toString(),
                KEY_TOTAL_TIME_TO_OPEN_PAYMENT_PAGE to getFormattedTime(paymentStartLoadTimestamp - checkoutTimestamp),
                KEY_PAYMENT_FINISH_LOAD_TIMESTAMP to paymentFinishLoadTimestamp.toString(),
                KEY_TOTAL_TIME_TO_LOAD_PAYMENT_PAGE to getFormattedTime(paymentFinishLoadTimestamp - paymentStartLoadTimestamp),
                KEY_TOTAL_TIME to getFormattedTime(paymentFinishLoadTimestamp - checkoutTimestamp)
            )
            ServerLogger.log(Priority.P2, TAG, data)
        }
    }

    private fun isSendLog(): Boolean {
        val thresholdStr = remoteConfig.getString(RemoteConfigKey.PAYMENT_OVERLONG_THRESHOLD, "")
        val threshold = thresholdStr.toLongOrDefault(FIVE_MINUTES_IN_MILIS)

        val timeDifference = paymentFinishLoadTimestamp - checkoutTimestamp
        return timeDifference >= threshold
    }

    private fun getFormattedTime(timestamp: Long): String {
        val millis: Long = timestamp % MILIS_DIVIDER
        val second: Long = timestamp / MILIS_DIVIDER % SECOND_DIVIDER
        val minute: Long = timestamp / (MILIS_DIVIDER * SECOND_DIVIDER) % MINUTE_DIVIDER
        val hour: Long =
            timestamp / (MILIS_DIVIDER * SECOND_DIVIDER * MINUTE_DIVIDER) % HOUR_DIVIDER

        return String.format(TIME_FORMAT, hour, minute, second, millis)
    }
}
