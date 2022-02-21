package com.tokopedia.otp.silentverification.view.dialog

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.otp.R

/**
 * Created by Yoris on 28/10/21.
 */

object SilentVerificationDialogUtils {
    fun showCellularDataDialog(context: Context, onPrimaryButtonClicked: () -> Unit, onSecondaryButtonClicked: () -> Unit) {
        DialogUnify(context, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(context.getString(com.tokopedia.otp.R.string.dialog_silent_verif_title))
            setDescription(context.getString(com.tokopedia.otp.R.string.dialog_silent_verif_desc))
            setPrimaryCTAText(context.getString(com.tokopedia.otp.R.string.dialog_silent_verif_lanjut))
            setSecondaryCTAText(context.getString(com.tokopedia.otp.R.string.dialog_silent_verif_batal))

            setPrimaryCTAClickListener {
                onPrimaryButtonClicked()
                dismiss()
            }

            setSecondaryCTAClickListener {
                onSecondaryButtonClicked()
                dismiss()
            }
            show()
        }
    }
}
