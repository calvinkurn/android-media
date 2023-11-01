package com.tokopedia.abstraction.base.view.nakamaupdate

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

class DownloadManagerUpdateDialogBuilder(
    private val updateModel: DownloadManagerUpdateModel
) {

    private var internalTestAlertDialog: AlertDialog? = null

    fun build(
        context: Context,
        onPositiveButtonClicked: (DialogInterface) -> Unit,
        onNegativeButtonClicked: (DialogInterface) -> Unit,
        onCancelableClicked: () -> Unit
    ): AlertDialog? {
        val internalTestDialog = internalTestAlertDialog
        return if (internalTestDialog == null) {
            val newInternalTestAlertDialog = AlertDialog.Builder(context)
                .setTitle(updateModel.dialogTitle)
                .setMessage(updateModel.dialogText)
                .setPositiveButton(updateModel.dialogButtonPositive, null)
                .setNegativeButton(
                    updateModel.dialogButtonNegative,
                    null
                )
                .setCancelable(true)
                .setOnCancelListener {
                    onCancelableClicked()
                }
                .create()

            newInternalTestAlertDialog.setOnShowListener { dialog: DialogInterface ->
                val positiveButton =
                    newInternalTestAlertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                val negativeButton =
                    newInternalTestAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                positiveButton?.setOnClickListener {
                    onPositiveButtonClicked(dialog)
                    dialog.dismiss()
                }
                negativeButton?.setOnClickListener {
                    dialog.dismiss()
                    onNegativeButtonClicked(dialog)
                }
            }

            internalTestAlertDialog = newInternalTestAlertDialog
            internalTestAlertDialog
        } else {
            internalTestAlertDialog
        }
    }
}
