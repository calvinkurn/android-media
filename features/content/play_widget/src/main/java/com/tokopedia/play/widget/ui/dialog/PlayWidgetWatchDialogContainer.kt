package com.tokopedia.play.widget.ui.dialog

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.play.widget.R


/**
 * Created by mzennis on 17/11/20.
 */
class PlayWidgetWatchDialogContainer {

    companion object {
        private const val CUSTOMER_APP_PACKAGE = "com.tokopedia.tkpd"
        private const val CUSTOMER_APP_PLAY_STORE = "https://play.google.com/store/apps/details?id=$CUSTOMER_APP_PACKAGE"
    }

    private lateinit var dialog: DialogUnify

    fun openPlayStore(context: Context) {
        getDialog(context).show()
    }

    private fun getDialog(context: Context): DialogUnify {
        if (!::dialog.isInitialized) {
            dialog = DialogUnify(
                    context = context,
                    actionType = DialogUnify.VERTICAL_ACTION,
                    imageType = DialogUnify.NO_IMAGE
            ).apply {
                setTitle(
                        context.getString(R.string.play_widget_watch_title)
                )
                setDescription(
                        context.getString(R.string.play_widget_watch_desc)
                )
                setPrimaryCTAText(
                        context.getString(R.string.play_widget_download_tokopedia)
                )
                setSecondaryCTAText(
                        context.getString(R.string.play_widget_watch_later)
                )
                setSecondaryCTAClickListener { dialog.dismiss() }
            }
        }
        dialog.setPrimaryCTAClickListener {
           context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(CUSTOMER_APP_PLAY_STORE)))
        }
        dialog.setSecondaryCTAClickListener {
            dialog.dismiss()
        }
        return dialog
    }
}