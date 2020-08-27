package com.tokopedia.statistic.common.utils.logger

import com.crashlytics.android.Crashlytics
import com.tokopedia.statistic.BuildConfig
import com.tokopedia.statistic.common.exception.StatisticException

/**
 * Created By @ilhamsuaib on 27/08/20
 */

object StatisticLogger {

    const val ERROR_LAYOUT = "Statistic - error get layout data."
    const val ERROR_WIDGET = "Statistic - error get widget data."

    fun logToCrashlytics(throwable: Throwable, message: String) {
        if (!BuildConfig.DEBUG) {
            val exceptionMessage = "$message - ${throwable.localizedMessage}"

            Crashlytics.logException(StatisticException(
                    message = exceptionMessage,
                    cause = throwable
            ))
        } else {
            throwable.printStackTrace()
        }
    }
}