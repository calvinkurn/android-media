package com.tokopedia.buyerorderdetail.presentation.dialog

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.unifycomponents.UnifyButton

class OrderExtensionDialog(
    context: Context,
    actionType: Int
) {

    private var dialogUnify: DialogUnify? = null

    init {
        dialogUnify = DialogUnify(
            context = context,
            actionType = actionType,
            imageType = DialogUnify.WITH_ILLUSTRATION
        )
    }

    fun getDialog() = dialogUnify

    fun setDescription(description: String) {
        dialogUnify?.setDescription(description)
    }

    fun setTitle(title: String) {
        dialogUnify?.setTitle(title)
    }

    fun setImageUrl(imageUrl: String) {
        dialogUnify?.setImageUrl(imageUrl)
    }

    fun setDialogSecondaryCta() {
        dialogUnify?.dialogSecondaryCTA?.apply {
            buttonVariant = UnifyButton.Variant.TEXT_ONLY
            buttonType = UnifyButton.Type.ALTERNATE
        }
    }

    fun dismissDialog() {
        if (dialogUnify?.isShowing == true)
            dialogUnify?.dismiss()
    }
}