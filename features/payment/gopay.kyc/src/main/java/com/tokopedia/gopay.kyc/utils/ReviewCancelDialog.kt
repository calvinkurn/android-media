package com.tokopedia.gopay.kyc.utils

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.gopay.kyc.R

object ReviewCancelDialog {

    fun showReviewDialog(context: Context, onContinue: () -> Unit, onExit: () -> Unit) {
        val dialog = DialogUnify(context, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(context.getString(R.string.review_cancel_dialog_title))
        dialog.setDescription(context.getString(R.string.review_cancel_dailog_description))
        dialog.setPrimaryCTAText(context.getString(R.string.continue_gopay_kyc_dialog))
        dialog.setPrimaryCTAClickListener {
            onContinue.invoke()
            dialog.dismiss()
        }
        dialog.setSecondaryCTAText(context.getString(R.string.exit_gopay_kyc))
        dialog.setSecondaryCTAClickListener {
            onExit.invoke()
            dialog.dismiss()
        }
        dialog.show()
    }
}