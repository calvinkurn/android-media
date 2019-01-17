package com.tokopedia.topchat.chatroom.view.customview

import android.content.Context
import android.view.View
import com.tokopedia.design.component.Dialog
import com.tokopedia.topchat.R
import javax.inject.Inject

/**
 * @author : Steven 04/01/19
 */
class TopChatRoomDialog @Inject constructor() {

        fun createAbortUploadImage(
                context: Context,
                alertDialog: Dialog,
                onOkListener: View.OnClickListener
        ): Dialog {

            alertDialog.setTitle(context.getString(R.string.exit_chat_title))
            alertDialog.setDesc(context.getString(R.string.exit_chat_body))
            alertDialog.setBtnCancel(context.getString(R.string.button_cancel))
            alertDialog.setBtnOk(context.getString(R.string.exit_chat_yes))
            alertDialog.setOnCancelClickListener {
                alertDialog.dismiss()
            }
            alertDialog.setOnOkClickListener(onOkListener)

            return alertDialog
        }

    }