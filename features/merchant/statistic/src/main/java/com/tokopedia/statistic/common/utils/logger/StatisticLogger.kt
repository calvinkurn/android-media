package com.tokopedia.statistic.common.utils.logger

import com.tokopedia.config.GlobalConfig
import com.tokopedia.statistic.common.exception.StatisticException
import com.google.firebase.crashlytics.FirebaseCrashlytics

/**
 * Created By @ilhamsuaib on 27/08/20
 */

object StatisticLogger {

    const val ERROR_LAYOUT = "Statistic - error get layout data."
    const val ERROR_WIDGET = "Statistic - error get widget data."
    const val ERROR_TICKER = "Statistic - error get ticker."
    const val ERROR_SELLER_ROLE = "Statistic - error get seller role."

    fun logToCrashlytics(throwable: Throwable, message: String) {
        if (!GlobalConfig.DEBUG) {
            val exceptionMessage = "$message - ${throwable.localizedMessage}"

            FirebaseCrashlytics.getInstance().recordException(StatisticException(
                    message = exceptionMessage,
                    cause = throwable
            ))
        } else {
            throwable.printStackTrace()
        }
    }
}