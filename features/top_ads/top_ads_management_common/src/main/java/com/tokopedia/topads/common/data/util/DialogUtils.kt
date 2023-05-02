package com.tokopedia.topads.common.data.util

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.topads.common.R

fun Context.showBidStateChangeConfirmationDialog(
    isSwitchedToAutomatic: Boolean, positiveClick: () -> Unit, negativeClick: () -> Unit,
) {
    DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
        if (isSwitchedToAutomatic) {
            setTitle(context.resources.getString(R.string.topads_common_manual_dialog_title))
            setDescription(context.resources.getString(R.string.topads_common_manual_dialog_description))
        } else {
            setTitle(context.resources.getString(R.string.topads_common_automatic_dialog_title))
            setDescription(context.resources.getString(R.string.topads_common_automatic_dialog_description))
        }
        setPrimaryCTAText(context.resources.getString(R.string.topads_common_dialog_cta_text))
        setSecondaryCTAText(context.resources.getString(R.string.topads_common_batal))
        setSecondaryCTAClickListener {
            negativeClick()
            dismiss()
        }
        setPrimaryCTAClickListener {
            positiveClick()
            dismiss()
        }
    }.show()
}
