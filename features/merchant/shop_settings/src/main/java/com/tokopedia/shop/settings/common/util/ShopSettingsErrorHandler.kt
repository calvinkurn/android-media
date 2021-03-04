package com.tokopedia.shop.settings.common.util

import android.content.Context
import android.text.TextUtils
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.network.data.model.response.ResponseV4ErrorException
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.settings.BuildConfig
import com.tokopedia.shop.settings.R
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ShopSettingsErrorHandler {

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
            } else if (e is RuntimeException && e.getLocalizedMessage() != null && e.getLocalizedMessage() != "" && e.localizedMessage?.length ?: 0 <= 3) {
                try {
                    e.localizedMessage?.let {
                        when (it.toInt()) {
                            400, 502 -> context?.getString(com.tokopedia.network.R.string.default_request_error_bad_request)
                            401 -> context?.getString(com.tokopedia.network.R.string.msg_expired_session_or_unauthorized)
                            403 -> context?.getString(com.tokopedia.network.R.string.default_request_error_forbidden_auth)
                            404 -> context?.getString(R.string.error_not_found_message)
                            408, 504 -> context?.getString(com.tokopedia.network.R.string.default_request_error_timeout)
                            429 -> context?.getString(R.string.error_full_visitor_message)
                            500 -> context?.getString(R.string.error_internal_server_error_message)
                            503 -> context?.getString(R.string.error_under_maintenance_message)
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