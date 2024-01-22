package com.tokopedia.sellerorder.common.presenter.dialogs

import android.content.Context
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.sellerorder.R
import com.tokopedia.unifycomponents.UnifyButton

class SomOrderHasOnGoingPofDialog(context: Context) {

    private val dialog: DialogUnify by lazy {
        DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.WITH_ILLUSTRATION).apply {
            if (DeviceScreenInfo.isTablet(context)) {
                dialogMaxWidth = getScreenWidth() / 2
            }
            setTitle(context.getString(R.string.som_pof_dialog_title_order_has_ongoing_pof))
            setDescription(context.getString(R.string.som_pof_dialog_subtitle_order_has_ongoing_pof))
            setImageDrawable(R.drawable.som_has_on_going_pof_dialog_illustration)
            setPrimaryCTAText(context.getString(R.string.som_pof_dialog_button_positive_order_has_ongoing_pof))
            setSecondaryCTAText(context.getString(R.string.som_pof_dialog_button_negative_order_has_ongoing_pof))
            dialogSecondaryCTA.buttonType = UnifyButton.Type.MAIN
            dialogSecondaryCTA.buttonVariant = UnifyButton.Variant.GHOST
            setOverlayClose(false)
            setCancelable(false)
        }
    }

    fun setupOnProceedAcceptOrder(onProceedAcceptOrder: () -> Unit) {
        dialog.setPrimaryCTAClickListener {
            onProceedAcceptOrder()
            dialog.dismiss()
        }
    }

    fun setupOnCancelAcceptOrder(onCancelAcceptOrder: () -> Unit) {
        dialog.setSecondaryCTAClickListener {
            onCancelAcceptOrder()
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
