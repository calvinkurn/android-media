package com.tokopedia.topchat.chatroom.view.adapter.viewholder.messagebubble.autoreply

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
import com.tokopedia.topchat.chatroom.view.custom.messagebubble.base.TopChatFlexBoxListener

/**
 * Layout guide:
 * { chat_bubble_unify_item }
 *      { partial_header_role_user }
 *      { partial_header_info }
 *      { TopChatChatroomMessageBubbleLayout /  partial_chat_message_bubble_auto_reply }
 *          { ReplyBubbleAreaMessage / partial_reply_bubble }
 *          { FlexBoxChatAutoReplyLayout / partial_flexbox_chat_bubble_auto_reply }
 *              { auto_reply_list }
 *                  { auto_reply_item } -> Auto Reply Text here
 */
class TopChatChatRoomAutoReplyViewHolder(
    itemView: View,
    msgClickLinkListener: ChatLinkHandlerListener,
    commonListener: CommonViewHolderListener,
    adapterListener: AdapterListener,
    chatMsgListener: TopChatFlexBoxListener,
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
        return itemView.findViewById(R.id.topchat_chatroom_bcl_message_bubble_auto_reply)
    }

    override fun getLayoutContainerBubble(): LinearLayout? {
        return itemView.findViewById(R.id.topchat_chatroom_ll_container_message_bubble_auto_reply)
    }

    override fun getMessageBubbleLayout(): BaseTopChatChatroomMessageBubbleLayout? {
        return itemView.findViewById(R.id.topchat_chatroom_message_bubble_layout_auto_reply)
    }

    override fun getFxChat(): BaseTopChatFlexBoxChatLayout? {
        return itemView.findViewById(R.id.fxChat)
    }

    companion object {
        val LAYOUT = R.layout.topchat_chatroom_chat_bubble_auto_reply_item
    }
}
