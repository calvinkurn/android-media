package com.tokopedia.tokofood.common.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.text.InputFilter
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.getBitmap
import com.tokopedia.network.constant.ResponseStatus
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodData
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


object TokofoodExt {

    const val NOT_FOUND_ERROR = "Not Found"
    const val INTERNAL_SERVER_ERROR = "Internal Server Error"
    const val ICON_BOUND_SIZE = 24

    const val MAXIMUM_QUANTITY = 999999
    const val MAXIMUM_QUANTITY_LENGTH = 7

    fun Throwable.getGlobalErrorType(): Int {
        return when (this) {
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

    fun <T : Parcelable> T.copyParcelable(): T? {
        var parcel: Parcel? = null

        return try {
            parcel = Parcel.obtain()
            parcel.writeParcelable(this, 0)
            parcel.setDataPosition(0)
            parcel.readParcelable(this::class.java.classLoader)
        } catch (throwable: Throwable) {
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
        val maxLength = InputFilter.LengthFilter(MAXIMUM_QUANTITY_LENGTH)
        editText.filters = arrayOf(maxLength)
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


    private fun scaledDrawable(bmp: Bitmap?, resources: Resources, width: Int, height: Int): Drawable? {
        return try {
            bmp?.let {
                val bmpScaled = Bitmap.createScaledBitmap(it, width, height, false)
                BitmapDrawable(resources, bmpScaled)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun Toolbar?.setBackIconUnify() {
        this?.context?.let {
            val backIconUnify = getIconUnifyDrawable(it, IconUnify.ARROW_BACK)?.getBitmap()
            val scaleDrawable: Drawable? =
                scaledDrawable(
                    backIconUnify,
                    resources,
                    ICON_BOUND_SIZE.toPx(),
                    ICON_BOUND_SIZE.toPx()
                )
            scaleDrawable?.let { newDrawable ->
                navigationIcon = newDrawable
            }
        }
    }
}