package com.tokopedia.shopdiscount.cancel

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.shopdiscount.R
import com.tokopedia.unifycomponents.UnifyButton

class CancelDiscountDialog(context: Context)  {

    private val dialog = DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
    private var onDeleteConfirmed: () -> Unit = {}


    fun setOnDeleteConfirmed(callback: () -> Unit) {
        this.onDeleteConfirmed = callback
    }

    fun show() = with(dialog) {
        setTitle(context.getString(R.string.sd_delete_confirmation_title))
        setDescription(context.getString(R.string.sd_delete_confirmation_description))
        setPrimaryCTAText(context.getString(R.string.sd_positive_delete))
        setPrimaryCTAClickListener {
            onDeleteConfirmed()
            dismiss()
        }
        setSecondaryCTAText(context.getString(R.string.sd_cancel))
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