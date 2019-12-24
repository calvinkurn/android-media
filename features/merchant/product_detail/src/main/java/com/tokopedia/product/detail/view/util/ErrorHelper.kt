package com.tokopedia.product.detail.view.util

import android.content.Context
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.product.detail.data.model.datamodel.PageErrorDataModel
import com.tokopedia.product.detail.data.model.datamodel.TobacoErrorData
import com.tokopedia.product.detail.data.util.TobacoErrorException

object ErrorHelper {

    private const val CODE_PRODUCT_ERR_NOT_FOUND = "2001"
    private const val CODE_ERR_GENERAL = "1"

    fun getErrorType(context: Context, t: Throwable): PageErrorDataModel {
        var shouldShowTobacoError = false
        var errorCode = "0"
        var tobacoErrorData: TobacoErrorData? = null

        when (t) {
            is MessageErrorException -> {
                shouldShowTobacoError = false
                errorCode = when (t.localizedMessage.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> {
                        GlobalError.NO_CONNECTION.toString()
                    }
                    ReponseStatus.NOT_FOUND -> {
                        GlobalError.PAGE_NOT_FOUND.toString()
                    }
                    ReponseStatus.INTERNAL_SERVER_ERROR -> {
                        GlobalError.SERVER_ERROR.toString()
                    }
                    else -> GlobalError.SERVER_ERROR.toString()
                }
            }
            is TobacoErrorException -> {
                shouldShowTobacoError = true
                tobacoErrorData = TobacoErrorData(t.messages, t.title, t.button, t.url)
            }
            else -> {
                shouldShowTobacoError = false
                errorCode = when (errorCode) {
                    CODE_PRODUCT_ERR_NOT_FOUND -> {
                        GlobalError.PAGE_NOT_FOUND.toString()
                    }
                    else -> {
                        CODE_ERR_GENERAL
                    }
                }
            }
        }

        return PageErrorDataModel(errorCode = errorCode, errorMessage = ErrorHandler.getErrorMessage(context, t), shouldShowTobacoError = shouldShowTobacoError, tobacoErrorData = tobacoErrorData)
    }
}