package com.tokopedia.buyerorderdetail.presentation.dialog

import android.content.Context
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.unifycomponents.UnifyButton

open class OrderExtensionDialog(
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
        dialogUnify?.setDescription(MethodChecker.fromHtml(description))
    }

    fun setTitle(title: String) {
        dialogUnify?.setTitle(title)
    }

    fun setImageUrl(imageUrl: String) {
        dialogUnify?.setImageUrl(imageUrl)
    }

    fun setDialogSecondaryCta() {
        dialogUnify?.dialogSecondaryLongCTA?.apply {
            val btnParams = this.layoutParams as? ViewGroup.MarginLayoutParams
            btnParams?.topMargin =
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
            layoutParams = btnParams
            buttonVariant = UnifyButton.Variant.GHOST
            buttonType = UnifyButton.Type.ALTERNATE
        }
    }

    fun dismissDialog() {
        if (dialogUnify?.isShowing == true)
            dialogUnify?.dismiss()
    }
}