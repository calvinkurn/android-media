package com.tokopedia.mvc.presentation.list.dialog

import android.content.Context
import android.view.ViewGroup
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.mvc.R

class StopVoucherConfirmationDialog(context: Context) {
    private val dialog = DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
    private var onPositiveConfirmed: () -> Unit = {}
    private var onNegativeConfirmed: () -> Unit = {}

    fun setOnPositiveConfirmed(callback: () -> Unit) {
        this.onPositiveConfirmed = callback
    }

    fun setOnNegativeConfirmed(callback: () -> Unit) {
        this.onNegativeConfirmed = callback
    }

    fun show(title : String, description : String, positiveLabel : String) = with(dialog) {
        setTitle(title)
        setDescMargin()
        setDescription(description)
        setPrimaryCTAText(positiveLabel)
        setPrimaryCTAClickListener {
            onPositiveConfirmed()
        }
        setSecondaryCTAText(context.getString(R.string.smvc_back))
        setSecondaryCTAClickListener {
            onNegativeConfirmed()
            setDismissDialog()
        }
        with(dialogSecondaryCTA) {
            buttonVariant = UnifyButton.Variant.TEXT_ONLY
            buttonType = UnifyButton.Type.ALTERNATE
        }
        show()
    }

    private fun setDescMargin(){
        dialog.dialogDesc.apply {
            val dialogParams = this.layoutParams as? ViewGroup.MarginLayoutParams
            dialogParams?.topMargin =
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
            layoutParams = dialogParams

        }
    }
    fun setLoadingProses(isLoading : Boolean){
        dialog.dialogPrimaryCTA.isLoading = isLoading
    }

    fun setCancelable(isCancelable : Boolean){
        dialog.setCancelable(isCancelable)
    }

    fun setDismissDialog(){
        if(!dialog.dialogPrimaryCTA.isLoading){
            dialog.dismiss()
        }
    }
}
