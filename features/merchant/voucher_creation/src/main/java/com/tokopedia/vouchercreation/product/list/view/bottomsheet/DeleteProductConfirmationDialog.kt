package com.tokopedia.vouchercreation.product.list.view.bottomsheet

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.vouchercreation.R


class DeleteProductConfirmationDialog(context: Context) {

    private val dialog = DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
    private var onDeleteProductConfirmed: () -> Unit = {}


    fun setOnDeleteProductConfirmed(onDeleteProductConfirmed: () -> Unit) {
        this.onDeleteProductConfirmed = onDeleteProductConfirmed
    }

    fun show() = with(dialog) {
        setTitle(context.getString(R.string.mvc_delete_product_confirmation_title))
        val description = context.getString(R.string.mvc_delete_product_confirmation_description)
        setDescription(description)
        setPrimaryCTAText(context.getString(R.string.mvc_delete_confirmation_yes))
        setPrimaryCTAClickListener {
            onDeleteProductConfirmed()
            dismiss()
        }
        setSecondaryCTAText(context.getString(R.string.mvc_delete_confirmation_no))
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