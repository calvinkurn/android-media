package com.tokopedia.minicart.bmgm.common.utils.logger

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.config.GlobalConfig
import timber.log.Timber


/**
 * Created By @ilhamsuaib on 27/08/20
 */

object NonFatalIssueLogger {

    private const val ERROR_MESSAGE_FORMAT = "[bmsm] OLP Mini Cart - %s - %s"

    fun logToCrashlytics(throwable: Throwable, place: String? = "") {
        if (GlobalConfig.DEBUG) {
            Timber.e(throwable)
        } else {
            val exception = MiniCartException(
                message = String.format(ERROR_MESSAGE_FORMAT, place, throwable.message),
                cause = throwable
            )
            FirebaseCrashlytics.getInstance().recordException(exception)
        }
    }
}