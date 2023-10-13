package com.tokopedia.topchat.chatroom.view.adapter.viewholder.messagebubble.regular

import android.view.View
import android.widget.LinearLayout
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.messagebubble.base.BaseTopChatBubbleMessageViewHolder
import com.tokopedia.topchat.chatroom.view.custom.message.ReplyBubbleAreaMessage
import com.tokopedia.topchat.chatroom.view.custom.messagebubble.base.BaseTopChatChatroomMessageBubbleLayout
import com.tokopedia.topchat.chatroom.view.custom.messagebubble.base.BaseTopChatFlexBoxChatLayout
import com.tokopedia.topchat.chatroom.view.custom.messagebubble.base.TopChatChatroomBubbleContainerLayout
import com.tokopedia.topchat.chatroom.view.custom.messagebubble.base.TopChatChatRoomFlexBoxListener

/**
 * Layout guide:
 * { chat_bubble_unify_item }
 *      { partial_header_role_user }
 *      { partial_header_info }
 *      { TopChatChatroomMessageBubbleLayout /  partial_chat_message_bubble }
 *          { ReplyBubbleAreaMessage / partial_reply_bubble }
 *          { TopChatChatRoomFlexBoxLayout / partial_flexbox_chat_bubble } -> Message text here
 */
class TopChatChatRoomBubbleMessageViewHolder(
    itemView: View,
    msgClickLinkListener: ChatLinkHandlerListener,
    commonListener: CommonViewHolderListener,
    adapterListener: AdapterListener,
    chatMsgListener: TopChatChatRoomFlexBoxListener,
    replyBubbleListener: ReplyBubbleAreaMessage.Listener
) : BaseTopChatBubbleMessageViewHolder<MessageUiModel>(
    itemView,
    msgClickLinkListener,
    commonListener,
    adapterListener,
    chatMsgListener,
    replyBubbleListener
) {

    override fun getBubbleChatLayout(): TopChatChatroomBubbleContainerLayout? {
        return itemView.findViewById(R.id.topchat_chatroom_bcl_message_bubble)
    }

    override fun getLayoutContainerBubble(): LinearLayout? {
        return itemView.findViewById(R.id.topchat_chatroom_ll_container_message_bubble)
    }

    override fun getMessageBubbleLayout(): BaseTopChatChatroomMessageBubbleLayout? {
        return itemView.findViewById(R.id.topchat_chatroom_message_bubble_layout)
    }

    override fun getFxChat(): BaseTopChatFlexBoxChatLayout? {
        return itemView.findViewById(R.id.fxChat)
    }

    override fun getHeaderInfo(): LinearLayout? {
        return itemView.findViewById(R.id.topchat_chatroom_ll_header_info_message_bubble)
    }

    companion object {
        val LAYOUT = R.layout.topchat_chatroom_chat_bubble_unify_item
        const val TYPE_BANNED = 2
    }
}
