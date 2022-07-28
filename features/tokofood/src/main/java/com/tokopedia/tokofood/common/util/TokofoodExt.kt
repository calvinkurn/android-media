package com.tokopedia.tokofood.common.util

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.network.constant.ResponseStatus
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodData
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifycomponents.Toaster
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object TokofoodExt {

    const val NOT_FOUND_ERROR = "Not Found"
    const val INTERNAL_SERVER_ERROR = "Internal Server Error"

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

    fun Throwable.getPostPurchaseGlobalErrorType(): Int {
        return when (this) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> GlobalError.NO_CONNECTION
            is MessageErrorException -> {
                when (localizedMessage) {
                    NOT_FOUND_ERROR -> GlobalError.PAGE_NOT_FOUND
                    INTERNAL_SERVER_ERROR -> GlobalError.SERVER_ERROR
                    else -> GlobalError.SERVER_ERROR
                }
            }
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

    fun <T: Parcelable> T.copyParcelable(): T? {
        var parcel: Parcel? = null

        return try {
            parcel = Parcel.obtain()
            parcel.writeParcelable(this, 0)
            parcel.setDataPosition(0)
            parcel.readParcelable(this::class.java.classLoader)
        } catch(throwable: Throwable) {
            null
        } finally {
            parcel?.recycle()
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

    fun QuantityEditorUnify.setupEditText() {
        editText.imeOptions = EditorInfo.IME_ACTION_DONE
        editText.setOnEditorActionListener { view, actionId, _ ->
            try {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    view.clearFocus()
                    val imm = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                    true
                } else false
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}