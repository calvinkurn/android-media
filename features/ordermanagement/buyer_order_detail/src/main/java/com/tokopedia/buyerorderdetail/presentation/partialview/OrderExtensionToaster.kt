package com.tokopedia.buyerorderdetail.presentation.partialview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import timber.log.Timber
import java.io.IOException
import java.lang.ref.WeakReference

class OrderExtensionToaster(
    private val context: Context?,
    val activity: WeakReference<Activity?>
) {

    fun showToaster(
        isOrderExtended: Boolean,
        message: String,
        view: View? = null,
        sendTracker: (() -> Unit)? = null
    ) {
        val toasterType =
            if (isOrderExtended) {
                Toaster.TYPE_NORMAL
            } else {
                Toaster.TYPE_ERROR
            }

        sendTracker?.invoke()

        showToasterCommon(
            view,
            message = message,
            toasterType = toasterType,
            isOrderExtended
        )
    }

    fun setToasterInternalError(view: View?, throwable: Throwable) {
        val messageError = if (throwable is IOException) {
            context?.getString(R.string.order_extension_message_error_connection).orEmpty()
        } else {
            context?.let { ErrorHandler.getErrorMessage(it, throwable) }.orEmpty()
        }
        showToasterCommon(
            view,
            messageError,
            Toaster.TYPE_ERROR,
            null,
        )
    }

    private fun showToasterCommon(
        view: View?,
        message: String,
        toasterType: Int,
        isOrderExtended: Boolean?
    ) {
        if (isOrderExtended == true) {
            val intent = Intent()
            intent.apply {
                putExtra(
                    ApplinkConstInternalOrder.OrderExtensionKey.TOASTER_MESSAGE,
                    message
                )
                putExtra(
                    ApplinkConstInternalOrder.OrderExtensionKey.IS_ORDER_EXTENDED,
                    isOrderExtended
                )
                putExtra(
                    ApplinkConstInternalOrder.OrderExtensionKey.TOASTER_TYPE, toasterType
                )
            }
            activity.get()?.setResult(Activity.RESULT_OK, intent)
            activity.get()?.finish()
        } else {
            showToasterServerError(
                view,
                toasterType,
                message
            )
        }
    }

    private fun showToasterServerError(view: View?, toasterType: Int, toasterMessage: String) {
        if (toasterMessage.isNotBlank()) {
            view?.run {
                val toaster = Toaster
                try {
                    toaster.toasterCustomBottomHeight =
                        resources.getDimensionPixelSize(com.tokopedia.unifycomponents.R.dimen.layout_lvl6)
                } catch (t: Throwable) {
                    Timber.d(t)
                }
                toaster.build(
                    this,
                    type = toasterType,
                    text = toasterMessage,
                    duration = Toaster.LENGTH_SHORT
                ).show()
            }
        }
    }
}