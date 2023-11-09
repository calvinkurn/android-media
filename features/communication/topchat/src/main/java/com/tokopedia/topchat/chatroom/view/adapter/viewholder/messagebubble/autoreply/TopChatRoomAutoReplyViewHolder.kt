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
import com.tokopedia.topchat.chatroom.view.custom.messagebubble.base.BaseTopChatRoomMessageBubbleLayout
import com.tokopedia.topchat.chatroom.view.custom.messagebubble.base.BaseTopChatFlexBoxChatLayout
import com.tokopedia.topchat.chatroom.view.custom.messagebubble.base.TopChatRoomBubbleContainerLayout
import com.tokopedia.topchat.chatroom.view.custom.messagebubble.base.TopChatRoomFlexBoxListener

/**
 * Layout guide:
 * { chat_bubble_unify_item }
 *      { partial_header_role_user }
 *      { partial_header_info }
 *      { TopChatRoomMessageBubbleLayout /  partial_chat_message_bubble_auto_reply }
 *          { ReplyBubbleAreaMessage / partial_reply_bubble }
 *          { TopChatRoomFlexBoxAutoReplyLayout / partial_flexbox_chat_bubble_auto_reply }
 *              { auto_reply_list }
 *                  { auto_reply_item } -> Auto Reply Text here
 */
class TopChatRoomAutoReplyViewHolder(
    itemView: View,
    msgClickLinkListener: ChatLinkHandlerListener,
    commonListener: CommonViewHolderListener,
    adapterListener: AdapterListener,
    chatMsgListener: TopChatRoomFlexBoxListener,
    replyBubbleListener: ReplyBubbleAreaMessage.Listener
) : BaseTopChatBubbleMessageViewHolder<MessageUiModel>(
    itemView,
    msgClickLinkListener,
    commonListener,
    adapterListener,
    chatMsgListener,
    replyBubbleListener
) {
    override fun getBubbleChatLayout(): TopChatRoomBubbleContainerLayout? {
        return itemView.findViewById(R.id.topchat_chatroom_bcl_message_bubble_auto_reply)
    }

    override fun getLayoutContainerBubble(): LinearLayout? {
        return itemView.findViewById(R.id.topchat_chatroom_ll_container_message_bubble_auto_reply)
    }

    override fun getMessageBubbleLayout(): BaseTopChatRoomMessageBubbleLayout? {
        return itemView.findViewById(R.id.topchat_chatroom_message_bubble_layout_auto_reply)
    }

    override fun getFxChat(): BaseTopChatFlexBoxChatLayout? {
        return itemView.findViewById(R.id.fxChat)
    }

    override fun getHeaderInfo(): LinearLayout? {
        return itemView.findViewById(R.id.topchat_chatroom_ll_header_info_message_bubble_auto_reply)
    }

    companion object {
        val LAYOUT = R.layout.topchat_chatroom_chat_bubble_auto_reply_item
    }
}
