package com.tokopedia.buyerorderdetail.presentation.dialog

import android.content.Context
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailOrderExtensionConstant
import com.tokopedia.dialog.DialogUnify

class OrderExtensionHasBeenSentDialog(
    private val context: Context,
    actionType: Int
) : OrderExtensionDialog(context, actionType) {

    fun showOrderHasBeenSentDialog() {
        val confirmedCancelledOrderDialog = OrderExtensionDialog(
            context,
            DialogUnify.SINGLE_ACTION
        ).apply {
            setTitle(context.getString(R.string.order_extension_title_order_has_been_sent))
            setDescription(context.getString(R.string.order_extension_desc_order_has_been_sent))
            setImageUrl(BuyerOrderDetailOrderExtensionConstant.Image.ORDER_HAS_BEEN_SENT_URL)
        }
        confirmedCancelledOrderDialog.getDialog()?.run {
            setPrimaryCTAText(context.getString(R.string.label_understand))
            setPrimaryCTAClickListener {
                dismiss()
            }
            show()
        }
    }
}