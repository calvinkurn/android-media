package com.tokopedia.appdownloadmanager_common.presentation.dialog

import android.content.Context
import android.net.Uri
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.appdownloadmanager_common.R
import com.tokopedia.appdownloadmanager_common.presentation.util.BaseDownloadManagerHelper.Companion.TOKOPEDIA_APK_PATH
import com.tokopedia.dialog.DialogUnify

object AppFileManagerDialog {

    fun showDialog(
        context: Context,
        fileNamePath: String,
        onSuccessDownload: () -> Unit
    ) {
        val fileUri = Uri.parse(fileNamePath)
        val apkName = fileUri.lastPathSegment

        val dialog = DialogUnify(
            context,
            actionType = DialogUnify.HORIZONTAL_ACTION,
            imageType = DialogUnify.NO_IMAGE
        )
        dialog.setTitle(context.getString(R.string.dialog_file_manager_title, TOKOPEDIA_APK_PATH))
        dialog.setDescription(MethodChecker.fromHtml(context.getString(R.string.dialog_file_manager_desc, apkName)))
        dialog.setPrimaryCTAText(context.getString(R.string.dialog_file_manager_btn_primary))
        dialog.setSecondaryCTAText(context.getString(R.string.dialog_file_manager_btn_secondary))

        dialog.setPrimaryCTAClickListener {
            onSuccessDownload.invoke()
            dialog.dismiss()
        }

        dialog.setSecondaryCTAClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
