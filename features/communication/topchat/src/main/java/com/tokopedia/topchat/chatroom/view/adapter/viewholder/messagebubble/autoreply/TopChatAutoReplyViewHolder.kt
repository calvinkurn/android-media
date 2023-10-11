package com.tokopedia.topchat.chatroom.view.adapter.viewholder.messagebubble.autoreply

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.util.MessageOnTouchListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.Payload
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.messagebubble.base.BaseTopChatBubbleMessageViewHolder
import com.tokopedia.topchat.chatroom.view.custom.message.ReplyBubbleAreaMessage
import com.tokopedia.topchat.chatroom.view.custom.messagebubble.autoreply.FlexBoxChatAutoReplyLayout
import com.tokopedia.topchat.chatroom.view.custom.messagebubble.autoreply.TopChatChatroomMessageBubbleAutoReplyLayout
import com.tokopedia.topchat.chatroom.view.custom.messagebubble.base.BaseTopChatFlexBoxChatLayout
import com.tokopedia.topchat.chatroom.view.custom.messagebubble.base.TopChatFlexBoxListener
import com.tokopedia.unifyprinciples.Typography

class TopChatAutoReplyViewHolder(
    itemView: View,
    msgClickLinkListener: ChatLinkHandlerListener,
    private val commonListener: CommonViewHolderListener,
    private val adapterListener: AdapterListener,
    private val chatMsgListener: TopChatFlexBoxListener,
    private val replyBubbleListener: ReplyBubbleAreaMessage.Listener
) : BaseTopChatBubbleMessageViewHolder<MessageUiModel>(itemView, commonListener) {

    private val messageBubble: TopChatChatroomMessageBubbleAutoReplyLayout? = itemView.findViewById(R.id.topchat_chatroom_message_bubble_layout_auto_reply)
    private val llMsgContainer: LinearLayout? = itemView.findViewById(R.id.topchat_chatroom_bcl_message_bubble_auto_reply)
    private val fxChat: FlexBoxChatAutoReplyLayout? = itemView.findViewById(R.id.fxChat)
    private val onTouchListener = MessageOnTouchListener(msgClickLinkListener)
    private val headerInfo: LinearLayout? = itemView.findViewById(R.id.topchat_chatroom_ll_header_info_message_bubble_auto_reply)

    private val headerRole: Typography? = itemView.findViewById(R.id.tvRole)
    private val smartReplyBlueDot: ImageView? = itemView.findViewById(R.id.topchat_chatroom_iv_header_role_blue_dot)
    private val header: LinearLayout? = itemView.findViewById(R.id.llRoleUser)
    private val bodyMsgContainer: LinearLayout? = itemView.findViewById(
        R.id.topchat_chatroom_ll_container_message_bubble_auto_reply
    )

    override fun getFxChat(): BaseTopChatFlexBoxChatLayout? {
        return fxChat
    }

    override fun bind(msg: MessageUiModel, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        when (payloads.first()) {
            Payload.REBIND -> bind(msg)
        }
    }

    override fun bind(uiModel: MessageUiModel) {
        super.bind(uiModel)
        bindListener(chatMsgListener)
        verifyReplyTime(uiModel)
        bindChatMessage(uiModel)
        bindOnTouchMessageListener(onTouchListener)
        bindHour(uiModel)
        bindReplyBubbleListener()
        bindReplyReference(uiModel)
        bindHeaderAttachment(uiModel)
        bindMargin(uiModel)
        bindClick()
        bindLongClick(uiModel)
        bindIcon(uiModel)
        bindTextColor(uiModel)
        if (uiModel.isSender) {
            // Right msg
            bindMsgGravity(Gravity.END)
            paddingRightMsg()
            bindBackground(bgRight)
            bindChatReadStatus(uiModel)
            bindHeader(uiModel)
            hideMessageInfo()
            hide(headerInfo)
        } else {
            // Left msg
            bindMsgGravity(Gravity.START)
            paddingLeftMsg()
            bindBackground(bgLeft)
            bindMessageInfo(uiModel)
            bindHeaderInfo(uiModel)
            hideReadStatus()
            hide(header)
        }
    }

    private fun bindMsgGravity(gravity: Int) {
        bindLayoutGravity(gravity)
        bindGravity(gravity)
        bindLayoutMsgGravity(gravity)
        messageBubble?.setMsgGravity(gravity)
    }

    private fun bindReplyBubbleListener() {
        messageBubble?.setReplyListener(replyBubbleListener)
    }

    private fun bindReplyReference(msg: MessageUiModel) {
        messageBubble?.bindReplyData(msg)
    }

    private fun paddingRightMsg() {
        llMsgContainer?.let {
            it.setPadding(
                0, it.paddingTop, bubbleToScreenMargin.toInt(), it.paddingBottom
            )
        }
    }

    private fun paddingLeftMsg() {
        llMsgContainer?.let {
            it.setPadding(
                bubbleToScreenMargin.toInt(), it.paddingTop, 0, it.paddingBottom
            )
        }
    }

    private fun verifyReplyTime(chat: MessageUiModel) {
        try {
            if (chat.replyTime.toLongOrZero() / MILISECONDS < START_YEAR) {
                chat.replyTime = (chat.replyTime.toLongOrZero() * MILISECONDS).toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bindMargin(message: MessageUiModel) {
        if (adapterListener.isOpposite(adapterPosition, message.isSender)) {
            llMsgContainer?.setMargin(0, topMarginOpposite.toInt(), 0, 0)
        } else {
            llMsgContainer?.setMargin(0, 0, 0, 0)
        }
    }

    private fun bindClick() {
        itemView.setOnClickListener { v ->
            KeyboardHandler.DropKeyboard(
                itemView.context,
                itemView
            )
        }
    }

    private fun bindGravity(gravity: Int) {
        llMsgContainer?.gravity = gravity
    }

    private fun bindLayoutGravity(gravity: Int) {
        val containerLp = llMsgContainer?.layoutParams as FrameLayout.LayoutParams
        containerLp.gravity = gravity
        llMsgContainer.layoutParams = containerLp
    }

    private fun bindLayoutMsgGravity(gravity: Int) {
        bodyMsgContainer?.gravity = gravity
    }

    private fun hide(view: View?) {
        view?.visibility = View.GONE
    }

    private fun show(view: View?) {
        view?.visibility = View.VISIBLE
    }

    private fun bindHeaderInfo(msg: MessageUiModel) {
        if (
            msg.source == BaseChatUiModel.SOURCE_REPLIED_BLAST &&
            commonListener.isSeller()
        ) {
            headerInfo?.show()
        } else {
            headerInfo?.hide()
        }
    }

    private fun bindHeader(message: MessageUiModel) {
        bindHeaderSmartReply(message)
        bindHeaderAutoReply(message)
        bindHeaderVisibility(message)
    }

    private fun bindHeaderSmartReply(message: MessageUiModel) {
        if (fromSmartReply(message)) {
            val headerText = itemView.context?.getString(R.string.tittle_header_smart_reply)
            headerRole?.text = headerText
        }
    }

    private fun bindHeaderAutoReply(message: MessageUiModel) {
        if (fromAutoReply(message)) {
            val headerText = itemView.context?.getString(R.string.tittle_header_auto_reply)
            headerRole?.text = headerText
        }
    }

    private fun bindHeaderVisibility(message: MessageUiModel) {
        if (fromAutoReply(message) || fromSmartReply(message)) {
            bindBlueDot(message)
            header?.show()
            headerRole?.show()
        } else {
            header?.hide()
        }
    }

    private fun fromAutoReply(msg: MessageUiModel): Boolean {
        return msg.isSender && commonListener.isSeller() && msg.isFromAutoReply()
    }

    private fun fromSmartReply(msg: MessageUiModel): Boolean {
        return msg.isSender && commonListener.isSeller() && msg.isFromSmartReply()
    }

    private fun bindBlueDot(message: MessageUiModel) {
        if (message.isFromSmartReply()) {
            smartReplyBlueDot?.show()
        } else {
            smartReplyBlueDot?.hide()
        }
    }

    companion object {
        val LAYOUT = R.layout.topchat_chatroom_chat_bubble_auto_reply_item
    }
}
