package com.tokopedia.shopadmin.common.utils

import android.graphics.Color
import android.text.method.LinkMovementMethod
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.network.constant.ResponseStatus
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Typography.setTextMakeHyperlink(text: String, onClick: () -> Unit) {
    val htmlString = HtmlLinkHelper(context, text)
    this.movementMethod = LinkMovementMethod.getInstance()
    this.highlightColor = Color.TRANSPARENT
    this.text = htmlString.spannedString
    htmlString.urlList.firstOrNull()?.setOnClickListener {
        onClick()
    }
}

fun Throwable.getGlobalErrorType(): Int {
    return when(this) {
        is SocketTimeoutException, is UnknownHostException, is ConnectException -> GlobalError.NO_CONNECTION
        is RuntimeException -> {
            when (localizedMessage?.toIntOrNull()) {
                ResponseStatus.SC_GATEWAY_TIMEOUT, ResponseStatus.SC_REQUEST_TIMEOUT -> GlobalError.NO_CONNECTION
                ResponseStatus.SC_NOT_FOUND -> GlobalError.PAGE_NOT_FOUND
                ResponseStatus.SC_INTERNAL_SERVER_ERROR -> GlobalError.SERVER_ERROR
                ResponseStatus.SC_BAD_GATEWAY -> GlobalError.MAINTENANCE
                else -> GlobalError.SERVER_ERROR
            }
        }
        else -> GlobalError.SERVER_ERROR
    }
}