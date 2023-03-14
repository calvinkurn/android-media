package com.tokopedia.chatbot.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.dynamicattachment.dynamicstickybutton.DynamicStickyButtonUiModel

class DynamicAttachmentTextViewHolder(
    itemView: View,
    private val chatLinkHandlerListener: ChatLinkHandlerListener,
):
    BaseChatBotViewHolder<DynamicStickyButtonUiModel>(itemView)  {

    private val movementMethod = ChatLinkHandlerMovementMethod(chatLinkHandlerListener)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_chatbot_chat_right
    }
}
