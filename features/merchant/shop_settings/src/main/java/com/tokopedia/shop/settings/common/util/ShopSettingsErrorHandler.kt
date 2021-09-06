package com.tokopedia.shop.settings.common.util

import android.content.Context
import android.text.TextUtils
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.network.data.model.response.ResponseV4ErrorException
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.settings.BuildConfig
import com.tokopedia.shop.settings.R
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ShopSettingsErrorHandler {

    private const val ERROR_CODE_TRY_AGAIN_1 = 400
    private const val ERROR_CODE_TRY_AGAIN_2 = 502
    private const val ERROR_CODE_SESSION_LOGIN_EXPIRED = 401
    private const val ERROR_CODE_CANNOT_ACCESS = 403
    private const val ERROR_CODE_DOESNT_EXIST = 404
    private const val ERROR_CODE_TIME_OUT_1 = 408
    private const val ERROR_CODE_TIME_OUT_2 = 504
    private const val ERROR_CODE_FULL_VISITORS = 429
    private const val ERROR_CODE_PROBLEM_NEED_TO_BE_FIXED = 500
    private const val ERROR_CODE_FIXING_PROBLEM = 503
    private const val MAX_LOCALIZED_MESSAGE = 3

    fun logMessage(message: String) {
        try {
            if (!BuildConfig.DEBUG) {
                FirebaseCrashlytics.getInstance().log(message)
            } else {
                Timber.e(message)
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun logExceptionToCrashlytics(t: Throwable) {
        try {
            FirebaseCrashlytics.getInstance().recordException(t)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun logExceptionToCrashlytics(m: String) {
        try {
            FirebaseCrashlytics.getInstance().log(m)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun getErrorMessage(context: Context?, e: Throwable?): String? {
        return if (e != null) {
            if (e is ResponseV4ErrorException) {
                e.errorList[0] as String
            } else if (e is UnknownHostException) {
                context?.getString(R.string.error_no_internet_message)
            } else if (e is SocketTimeoutException) {
                context?.getString(com.tokopedia.network.R.string.default_request_error_timeout)
            } else if (e is RuntimeException && e.getLocalizedMessage() != null && e.getLocalizedMessage() != "" && e.localizedMessage?.length.orZero() <= MAX_LOCALIZED_MESSAGE) {
                try {
                    e.localizedMessage?.let {
                        when (it.toInt()) {
                            ERROR_CODE_TRY_AGAIN_1, ERROR_CODE_TRY_AGAIN_2 -> context?.getString(com.tokopedia.network.R.string.default_request_error_bad_request)
                            ERROR_CODE_SESSION_LOGIN_EXPIRED -> context?.getString(com.tokopedia.network.R.string.msg_expired_session_or_unauthorized)
                            ERROR_CODE_CANNOT_ACCESS -> context?.getString(com.tokopedia.network.R.string.default_request_error_forbidden_auth)
                            ERROR_CODE_DOESNT_EXIST -> context?.getString(R.string.error_not_found_message)
                            ERROR_CODE_TIME_OUT_1, ERROR_CODE_TIME_OUT_2 -> context?.getString(com.tokopedia.network.R.string.default_request_error_timeout)
                            ERROR_CODE_FULL_VISITORS -> context?.getString(R.string.error_full_visitor_message)
                            ERROR_CODE_PROBLEM_NEED_TO_BE_FIXED -> context?.getString(R.string.error_internal_server_error_message)
                            ERROR_CODE_FIXING_PROBLEM -> context?.getString(R.string.error_under_maintenance_message)
                            else -> context?.getString(com.tokopedia.network.R.string.default_request_error_unknown)
                        }
                    }
                } catch (var3: NumberFormatException) {
                    context?.getString(com.tokopedia.network.R.string.default_request_error_unknown)
                }
            } else if (e is MessageErrorException && !TextUtils.isEmpty(e.message)) {
                e.message
            } else if (e is com.tokopedia.abstraction.common.network.exception.MessageErrorException && !e.message.isNullOrBlank()) {
                e.message
            } else {
                if (e is IOException) context?.getString(R.string.error_internal_server_error_message) else context?.getString(com.tokopedia.network.R.string.default_request_error_unknown)
            }
        } else {
            context?.getString(R.string.error_unknown_issue)
        }
    }
}