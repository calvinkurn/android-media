package com.tokopedia.ordermanagement.buyercancellationorder.common.utils

import com.tokopedia.globalerror.GlobalError
import com.tokopedia.ordermanagement.buyercancellationorder.common.constants.BuyerConsts
import com.tokopedia.unifycomponents.ticker.Ticker
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object BuyerUtils {

    @JvmStatic
    fun getTickerType(typeStr: String): Int {
        return when (typeStr) {
            BuyerConsts.TICKER_TYPE_ERROR -> {
                Ticker.TYPE_ERROR
            }
            BuyerConsts.TICKER_TYPE_INFORMATION -> {
                Ticker.TYPE_INFORMATION
            }
            BuyerConsts.TICKER_TYPE_WARNING -> {
                Ticker.TYPE_WARNING
            }
            BuyerConsts.TICKER_TYPE_ANNOUNCEMENT -> {
                Ticker.TYPE_ANNOUNCEMENT
            }
            else -> {
                Ticker.TYPE_INFORMATION
            }
        }
    }

    fun Throwable.getGlobalErrorType(): Int {
        return when (this) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> GlobalError.NO_CONNECTION
            else -> GlobalError.SERVER_ERROR
        }
    }
}
