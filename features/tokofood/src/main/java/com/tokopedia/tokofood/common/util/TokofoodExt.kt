package com.tokopedia.tokofood.common.util

import android.view.View
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.network.constant.ResponseStatus
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodData
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.unifycomponents.Toaster
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object TokofoodExt {

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

    fun Any.getSuccessUpdateResultPair(): Pair<UpdateParam, CartTokoFoodData>? {
        return (this as? Pair<*, *>)?.let { pair ->
            (pair.first as? UpdateParam)?.let { updateParams ->
                (pair.second as? CartTokoFoodData)?.let { cartTokoFoodData ->
                    updateParams to cartTokoFoodData
                }
            }
        }
    }

    fun View?.showErrorToaster(errorMessage: String) {
        if (errorMessage.isNotBlank()) {
            this?.let {
                Toaster.build(
                    it,
                    text = errorMessage,
                    duration = Toaster.LENGTH_SHORT,
                    type = Toaster.TYPE_ERROR
                ).show()
            }
        }
    }

}