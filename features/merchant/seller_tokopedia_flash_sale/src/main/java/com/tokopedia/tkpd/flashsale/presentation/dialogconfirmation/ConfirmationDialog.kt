package com.tokopedia.tkpd.flashsale.presentation.dialogconfirmation

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.seller_tokopedia_flash_sale.R

class ConfirmationDialog(context: Context) {
    private val dialog = DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
    private var onPositiveConfirmed: () -> Unit = {}

    fun setOnPositiveConfirmed(callback: () -> Unit) {
        this.onPositiveConfirmed = callback
    }

    fun show(title : String, description : String, positiveLable : String) = with(dialog) {
        setTitle(title)
        setDescription(description)
        setPrimaryCTAText(positiveLable)
        setPrimaryCTAClickListener {
            onPositiveConfirmed()
            dismiss()
        }
        setSecondaryCTAText(context.getString(R.string.fs_canceled_lable))
        setSecondaryCTAClickListener {
            dismiss()
        }
        with(dialogSecondaryCTA) {
            buttonVariant = UnifyButton.Variant.TEXT_ONLY
            buttonType = UnifyButton.Type.ALTERNATE
        }
        show()
    }
}
