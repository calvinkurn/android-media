package com.tokopedia.tokopedianow.common.util

import android.app.Activity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopedianow.R
import java.net.UnknownHostException

internal object GlobalErrorUtil {
    const val ERROR_PAGE_NOT_FOUND = "404"
    const val ERROR_SERVER = "500"
    const val ERROR_PAGE_FULL = "501"
    const val ERROR_MAINTENANCE = "502"

    fun getErrorType(throwable: Throwable?, errorCode: String) = when{
        throwable is UnknownHostException -> GlobalError.NO_CONNECTION
        errorCode == ERROR_PAGE_FULL -> GlobalError.PAGE_FULL
        errorCode == ERROR_SERVER -> GlobalError.SERVER_ERROR
        errorCode == ERROR_MAINTENANCE -> GlobalError.MAINTENANCE
        errorCode == ERROR_PAGE_NOT_FOUND -> GlobalError.PAGE_NOT_FOUND
        else -> GlobalError.SERVER_ERROR
    }

    fun GlobalError.setupLayout(
        throwable: Throwable?,
        onClickRetryListener: () -> Unit
    ) {
        if (throwable is MessageErrorException && throwable.errorCode != null) {
            val errorType = getErrorType(
                throwable = throwable,
                errorCode = throwable.errorCode
            )
            setType(errorType)
            when (throwable.errorCode) {
                ERROR_PAGE_NOT_FOUND -> setupGlobalErrorPageNotFound()
                ERROR_SERVER -> setupActionClickListener(onClickRetryListener)
                ERROR_MAINTENANCE -> setupServerMaintenance()
                ERROR_PAGE_FULL -> setupActionClickListener(onClickRetryListener)
                else -> setupActionClickListener(onClickRetryListener)
            }
        } else {
            val errorType = getErrorType(
                throwable = throwable,
                errorCode = String.EMPTY
            )
            setType(errorType)
            setupActionClickListener(onClickRetryListener)
        }
    }

    private fun GlobalError.setupGlobalErrorPageNotFound() {
        errorAction.text = context.getString(R.string.tokopedianow_common_error_state_button_back_to_tokonow_home_page)
        errorSecondaryAction.show()
        errorSecondaryAction.text = context.getString(R.string.tokopedianow_common_empty_state_button_return)
        setActionClickListener {
            RouteManager.route(context, ApplinkConstInternalTokopediaNow.HOME)
            if (context is Activity) (context as Activity).finish()
        }
        setSecondaryActionClickListener {
            RouteManager.route(context, ApplinkConst.HOME)
            if (context is Activity) (context as Activity).finish()
        }
    }

    private fun GlobalError.setupServerMaintenance() {
        errorAction.show()
        errorAction.text = context.getString(R.string.tokopedianow_common_error_state_button_back_to_tokonow_home_page)
        setActionClickListener {
            RouteManager.route(context, ApplinkConstInternalTokopediaNow.HOME)
            if (context is Activity) (context as Activity).finish()
        }
    }

    private fun GlobalError.setupActionClickListener(
        onClickRetryListener: () -> Unit
    ) {
        setActionClickListener {
            onClickRetryListener.invoke()
        }
    }
}
