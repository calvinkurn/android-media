package com.tokopedia.shopadmin.invitationconfirmation.presentation.dialog

import android.content.Context
import com.tokopedia.dialog.DialogUnify

class AdminInvitationConfirmRejectDialog(
    context: Context
) {

    private var dialogUnify: DialogUnify? = null

    init {
        dialogUnify = DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
    }

    fun getDialog() = dialogUnify

    fun setDescription(description: String) {
        dialogUnify?.setDescription(description)
    }

    fun setTitle(title: String) {
        dialogUnify?.setTitle(title)
    }

    fun setPrimaryTitle(title: String) {
        dialogUnify?.setPrimaryCTAText(title)
    }

    fun setSecondaryTitle(title: String) {
        dialogUnify?.setSecondaryCTAText(title)
    }

    fun dismissDialog() {
        if (dialogUnify?.isShowing == true)
            dialogUnify?.dismiss()
    }
}