package com.tokopedia.vouchercreation.create.view.dialog

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.vouchercreation.R

class CreateVoucherCancelDialog(context: Context,
                                private val onSecondaryClick: () -> Unit) {

    private val dialog = DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)

    fun show() = with(dialog) {
        setTitle(context.getString(R.string.mvc_create_prompt_back_title))
        setDescription(context.getString(R.string.mvc_create_prompt_back_desc))
        setPrimaryCTAText(context.getString(R.string.mvc_back))
        setPrimaryCTAClickListener {
            dismiss()
        }
        setSecondaryCTAText(context.getString(R.string.mvc_yes_cancel))
        setSecondaryCTAClickListener {
            onSecondaryClick()
            dismiss()
        }
        show()
    }

}