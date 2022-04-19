package com.tokopedia.topchat.chatroom.view.customview

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import javax.inject.Inject

/**
 * @author : Steven 04/01/19
 */
class TopChatRoomDialog @Inject constructor() {

    fun createAbortUploadImage(
            context: Context,
            onCTAClickListener: () -> Unit
    ): DialogUnify {
        val title = context.getString(com.tokopedia.chat_common.R.string.exit_chat_title)
        val desc = context.getString(com.tokopedia.chat_common.R.string.exit_chat_body)
        val primaryCta = context.getString(com.tokopedia.chat_common.R.string.exit_chat_yes)
        val secondaryCta = context.getString(com.tokopedia.chat_common.R.string.button_cancel)
        return DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(title)
            setDescription(desc)
            setPrimaryCTAText(primaryCta)
            setSecondaryCTAText(secondaryCta)
            setSecondaryCTAClickListener {
                dismiss()
            }
            setPrimaryCTAClickListener {
                onCTAClickListener()
            }
        }
    }

}