package com.tokopedia.mvc.presentation.list.dialog

import android.content.Context
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.unifycomponents.UnifyButton

class CallTokopediaCareDialog(context: Context) {

    companion object {
        private const val imageCallTokopediaCare = "https://images.tokopedia.net/img/android/campaign/merchant-voucher-creation/contact_cs_campaign_voucher_creation.png"
    }
    private var dialogUnify: DialogUnify? = null
    private var onPositiveConfirmed: () -> Unit = {}
    private var onNegativeConfirmerd: () -> Unit = {
        dismissDialog()
    }

    init {
        dialogUnify = DialogUnify(
            context = context,
            actionType = DialogUnify.VERTICAL_ACTION,
            imageType = DialogUnify.WITH_ILLUSTRATION
        )

        dialogUnify?.apply {
            setImageUrl(imageCallTokopediaCare)
        }
    }

    fun getDialog() = dialogUnify

    fun setTitle(title: String) {
        dialogUnify?.setTitle(title)
    }

    fun setDescription(description: String) {
        dialogUnify?.setDescription(MethodChecker.fromHtml(description))
    }

    fun setOnPositiveConfirmed(callback: () -> Unit) {
        this.onPositiveConfirmed = callback
    }

    fun show(primaryCtaText: String, secondaryCtaText: String){
        dialogUnify?.apply {
            setDialogSecondaryCta()
            setPrimaryCTAText(primaryCtaText)
            setPrimaryCTAClickListener {
                onPositiveConfirmed()
                dismissDialog()
            }
            setSecondaryCTAText(secondaryCtaText)
            setSecondaryCTAClickListener {
                onNegativeConfirmerd()
            }
            show()
        }
    }

    private fun setDialogSecondaryCta() {
        dialogUnify?.dialogSecondaryLongCTA?.apply {
            val btnParams = this.layoutParams as? ViewGroup.MarginLayoutParams
            btnParams?.topMargin =
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
            layoutParams = btnParams
            buttonVariant = UnifyButton.Variant.TEXT_ONLY
            buttonType = UnifyButton.Type.ALTERNATE
        }
    }

    private fun dismissDialog() {
        if (dialogUnify?.isShowing == true)
            dialogUnify?.dismiss()
    }
}
