package com.tokopedia.shop_widget.buy_more_save_more.util

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.config.GlobalConfig
import timber.log.Timber

object NonFatalIssueLogger {
    private const val ERROR_MESSAGE_FORMAT = "[bmsm] Widget - %s - %s"

    fun logToCrashlytics(throwable: Throwable, place: String? = "") {
        if (GlobalConfig.DEBUG) {
            Timber.e(throwable)
        } else {
            val exception = BmsmWidgetException(
                message = String.format(ERROR_MESSAGE_FORMAT, place, throwable.message),
                cause = throwable
            )
            FirebaseCrashlytics.getInstance().recordException(exception)
        }
    }
}
