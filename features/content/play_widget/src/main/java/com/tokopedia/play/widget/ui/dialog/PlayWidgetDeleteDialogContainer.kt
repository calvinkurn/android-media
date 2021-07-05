package com.tokopedia.play.widget.ui.dialog

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.play.widget.R

/**
 * Created by jegul on 09/11/20
 */
class PlayWidgetDeleteDialogContainer(private val listener: Listener) {

    private lateinit var dialog: DialogUnify

    fun confirmDelete(context: Context, channelId: String) {
        getDialog(context, channelId).show()
    }

    private fun getDialog(context: Context, channelId: String): DialogUnify {
        if (!::dialog.isInitialized) {
            dialog = DialogUnify(
                    context = context,
                    actionType = DialogUnify.HORIZONTAL_ACTION,
                    imageType = DialogUnify.NO_IMAGE
            ).apply {
                setTitle(
                        context.getString(R.string.play_widget_delete_confirmation_title)
                )
                setDescription(
                        context.getString(R.string.play_widget_delete_confirmation_desc)
                )
                setPrimaryCTAText(
                        context.getString(R.string.play_widget_delete)
                )
                setSecondaryCTAText(
                        context.getString(R.string.play_widget_delete_cancel)
                )
                setSecondaryCTAClickListener { dialog.dismiss() }
            }
        }
        dialog.setPrimaryCTAClickListener {
            listener.onDeleteButtonClicked(channelId)
            dialog.dismiss()
        }
        return dialog
    }

    interface Listener {

        fun onDeleteButtonClicked(channelId: String)
    }
}