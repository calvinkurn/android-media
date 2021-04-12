package com.tokopedia.sellerorder.common.presenter.dialogs

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.sellerorder.R
import com.tokopedia.unifycomponents.UnifyButton

class SomOrderHasRequestCancellationDialog(context: Context) {

    private val dialog: DialogUnify by lazy {
        DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.WITH_ILLUSTRATION).apply {
            setTitle(context.getString(R.string.som_error_dialog_title_order_has_cancellation_request))
            setDescription(context.getString(R.string.som_error_dialog_subtitle_order_has_cancellation_request))
            setImageDrawable(R.drawable.ic_som_list_dialog_error_order_has_request_cancellation)
            setPrimaryCTAText(context.getString(R.string.see_detail))
            dialogSecondaryCTA.buttonType = UnifyButton.Type.ALTERNATE
            dialogSecondaryCTA.buttonVariant = UnifyButton.Variant.TEXT_ONLY
            setOverlayClose(false)
            setCancelable(false)
        }
    }

    fun setupActionButton(actionName: String, onActionButtonClicked: () -> Unit) {
        dialog.setSecondaryCTAText(actionName)
        dialog.setSecondaryCTAClickListener {
            onActionButtonClicked.invoke()
            dialog.dismiss()
        }
    }

    fun setupGoToOrderDetailButton(onGoToOrderDetailButtonClicked: () -> Unit) {
        dialog.setPrimaryCTAClickListener {
            onGoToOrderDetailButtonClicked.invoke()
            dialog.dismiss()
        }
    }

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }
}