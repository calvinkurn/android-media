package com.tokopedia.mvc.presentation.product.variant.dialog

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.mvc.R
import com.tokopedia.unifycomponents.UnifyButton

object ConfirmationDialog {

    fun show(
        context: Context,
        title: String,
        description: String,
        primaryButtonTitle: String,
        onPrimaryButtonClick: () -> Unit
    ) {
        val dialog = DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        with(dialog) {

            setTitle(title)
            setDescription(description)

            setPrimaryCTAText(primaryButtonTitle)
            setPrimaryCTAClickListener {
                onPrimaryButtonClick()
                dismiss()
            }

            setSecondaryCTAText(context.getString(R.string.smvc_cancel))
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
}
