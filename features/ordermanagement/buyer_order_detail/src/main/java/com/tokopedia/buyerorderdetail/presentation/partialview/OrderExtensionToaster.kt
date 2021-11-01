package com.tokopedia.buyerorderdetail.presentation.partialview

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderExtensionConstant
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import java.lang.ref.WeakReference

class OrderExtensionToaster(
    private val context: Context?,
    val activity: WeakReference<Activity?>
) {

    fun setToasterNormal(
        messageCode: Int,
        message: String,
        sendTracker: ((Boolean) -> Unit)? = null
    ) {
        val isOrderExtended = messageCode == BuyerOrderExtensionConstant.RespondMessageCode.SUCCESS
        val toasterType =
            if (messageCode ==
                BuyerOrderExtensionConstant.RespondMessageCode.SUCCESS
            ) {
                Toaster.TYPE_NORMAL
            } else {
                Toaster.TYPE_ERROR
            }

        sendTracker?.invoke(isOrderExtended)

        showToasterCommon(
            message = message,
            toasterType = toasterType,
            isOrderExtended
        )
    }

    fun setToasterInternalError(throwable: Throwable) {
        showToasterCommon(
            context?.let { ErrorHandler.getErrorMessage(it, throwable) }.orEmpty(),
            Toaster.TYPE_ERROR,
            null,
        )
    }

    private fun showToasterCommon(
        message: String,
        toasterType: Int,
        isOrderExtended: Boolean?
    ) {
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
    }
}