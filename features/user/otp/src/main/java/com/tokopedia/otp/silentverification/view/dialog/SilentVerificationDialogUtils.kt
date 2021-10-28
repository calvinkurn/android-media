package com.tokopedia.otp.silentverification.view.dialog

import android.content.Context
import com.tokopedia.dialog.DialogUnify

/**
 * Created by Yoris on 28/10/21.
 */

object SilentVerificationDialogUtils {
    fun showCellularDataDialog(context: Context, onPrimaryButtonClicked: () -> Unit, onSecondaryButtonClicked: () -> Unit) {
        DialogUnify(context, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle("Pastikan paket datamu aktif")
            setDescription("Kamu perlu paket data untuk pakai metode verifikasi ini.")
            setPrimaryCTAText("Lanjut Verifikasi")
            setSecondaryCTAText("Batal")

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
