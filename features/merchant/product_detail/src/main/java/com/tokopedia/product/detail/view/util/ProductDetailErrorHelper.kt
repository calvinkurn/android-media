package com.tokopedia.product.detail.view.util

import android.content.Context
import android.os.Handler
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.data.model.datamodel.PageErrorDataModel
import com.tokopedia.product.detail.data.model.datamodel.TobacoErrorData
import com.tokopedia.product.detail.data.util.TobacoErrorException
import com.tokopedia.url.TokopediaUrl
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

object ProductDetailErrorHelper {

    private const val CODE_PRODUCT_ERR_NOT_FOUND = "2001"
    private const val CODE_PRODUCT_ERR_NOT_FOUND_GENERAL = "400"
    private const val CODE_ERR_GENERAL = "1"
    private const val DEEPLINK_PREFIX = "tokopedia://marketplace/product-detail/"
    const val CODE_PRODUCT_ERR_BANNED = "2998"
    const val CODE_PRODUCT_ERR_DELETED = "3000"
    const val CODE_PRODUCT_ERR_KELONTONG = "3005"

    private val links: MutableList<String> = arrayListOf()

    fun getErrorType(context: Context, t: Throwable, fromDeeplink: Boolean, deeplinkUrl: String): PageErrorDataModel {
        var shouldShowTobacoError = false
        var errorCode = "0"
        var tobacoErrorData: TobacoErrorData? = null

        when (t) {
            is MessageErrorException -> {
                shouldShowTobacoError = false
                errorCode = when (t.errorCode) {
                    CODE_PRODUCT_ERR_NOT_FOUND, CODE_PRODUCT_ERR_DELETED, CODE_PRODUCT_ERR_KELONTONG -> {
                        if (fromDeeplink && t.errorCode == CODE_PRODUCT_ERR_NOT_FOUND) {
                            logDeeplinkError(deeplinkUrl, t.errorCode.toIntOrZero())
                        }

                        GlobalError.PAGE_NOT_FOUND.toString()
                    }
                    CODE_PRODUCT_ERR_BANNED -> {
                        CODE_PRODUCT_ERR_BANNED
                    }
                    else -> {
                        if (fromDeeplink && t.errorCode == CODE_PRODUCT_ERR_NOT_FOUND_GENERAL) {
                            logDeeplinkError(deeplinkUrl, t.errorCode.toIntOrZero())
                        }
                        CODE_ERR_GENERAL
                    }
                }
            }
            is TobacoErrorException -> {
                shouldShowTobacoError = true
                tobacoErrorData = TobacoErrorData(t.messages, t.title, t.button, t.url)
            }
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                errorCode = GlobalError.NO_CONNECTION.toString()
            }
            is RuntimeException -> {
                errorCode = when (t.localizedMessage?.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> {
                        GlobalError.NO_CONNECTION.toString()
                    }
                    ReponseStatus.NOT_FOUND, CODE_PRODUCT_ERR_NOT_FOUND.toInt() -> {
                        GlobalError.PAGE_NOT_FOUND.toString()
                    }
                    ReponseStatus.INTERNAL_SERVER_ERROR -> {
                        GlobalError.SERVER_ERROR.toString()
                    }
                    else -> {
                        GlobalError.SERVER_ERROR.toString()
                    }
                }
            }
            else -> {
                shouldShowTobacoError = false
                errorCode = CODE_ERR_GENERAL
            }
        }

        return PageErrorDataModel(errorCode = errorCode, errorMessage = ErrorHandler.getErrorMessage(context, t), shouldShowTobacoError = shouldShowTobacoError, tobacoErrorData = tobacoErrorData)
    }

    private fun logDeeplinkError(deeplinkUrl: String = "", errorCode: Int) {
        val weblink = convertToWeblink(deeplinkUrl)
        if (!links.contains(weblink)) {
            ServerLogger.log(Priority.P2, "PDP_OPEN_DEEPLINK_ERROR", mapOf("type" to weblink, "errorCode" to errorCode.toString()))
            links.add(weblink)

            Handler().postDelayed({
                links.remove(weblink)
            }, TimeUnit.MINUTES.toMillis(5))
        }
    }

    private fun convertToWeblink(deeplinkUrl: String): String {
        return deeplinkUrl.replace(DEEPLINK_PREFIX, TokopediaUrl.getInstance().WEB)
    }
}