package com.tokopedia.editor.ui.dialog

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.editor.R
import javax.inject.Inject

class ConfirmationDialog @Inject constructor() {

    fun show(context: Context, onPrimaryClick: () -> Unit) {
        val dialog = DialogUnify(
            context = context,
            actionType = DialogUnify.VERTICAL_ACTION,
            imageType = DialogUnify.NO_IMAGE
        )

        dialog.apply {
            setTitle(context.getString(R.string.universal_editor_main_confirmation_title))
            setDescription(context.getString(R.string.universal_editor_main_confirmation_desc))

            dialogPrimaryCTA.apply {
                text = context.getString(R.string.universal_editor_main_confirmation_primary_cta)
                setOnClickListener { onPrimaryClick() }
            }

            dialogSecondaryLongCTA.apply {
                text = context.getString(R.string.universal_editor_main_confirmation_secondary_cta)
                setOnClickListener {
                    dismiss()
                }
            }
        }

        dialog.show()
    }
}
