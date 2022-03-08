package com.tokopedia.topads.edit.utils

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.topads.edit.R

fun showEditSwitchToggledDialog(
    context: Context, switchToManual: Boolean, positiveClick: () -> Unit, cancel: () -> Unit,
) {
    DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
        if (switchToManual) {
            setTitle(context.resources.getString(R.string.topads_edit_manual_dialog_title))
            setDescription(context.resources.getString(R.string.topads_edit_manual_dialog_description))
        } else {
            setTitle(context.resources.getString(R.string.topads_edit_automatic_dialog_title))
            setDescription(context.resources.getString(R.string.topads_edit_automatic_dialog_description))
        }
        setPrimaryCTAText(context.resources.getString(R.string.topads_edit_batal))
        setSecondaryCTAText(context.resources.getString(R.string.topads_edit_dialog_cta_text))
        setSecondaryCTAClickListener {
            cancel()
            dismiss()
        }
        setPrimaryCTAClickListener {
            positiveClick()
            dismiss()
        }
    }.show()
}