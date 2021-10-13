package com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
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
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.ChatMessageViewHolderBinder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.ChatMessageViewHolderBinder.generateLeftBg
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.ChatMessageViewHolderBinder.generateRightBg
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.getOppositeMargin
import com.tokopedia.topchat.chatroom.view.custom.FlexBoxChatLayout
import com.tokopedia.topchat.chatroom.view.custom.MessageBubbleLayout
import com.tokopedia.topchat.chatroom.view.custom.message.ReplyBubbleAreaMessage
import com.tokopedia.unifyprinciples.Typography

class ChatMessageUnifyViewHolder(
    itemView: View?,
    protected val msgCliclLinkListener: ChatLinkHandlerListener,
    private val commonListener: CommonViewHolderListener,
    private val adapterListener: AdapterListener,
    private val chatMsgListener: FlexBoxChatLayout.Listener,
    private val replyBubbleListener: ReplyBubbleAreaMessage.Listener
) : BaseChatViewHolder<MessageViewModel>(itemView) {

    private val messageBubble: MessageBubbleLayout? = itemView?.findViewById(R.id.mb_bubble_msg)
    private val llMsgContainer: LinearLayout? = itemView?.findViewById(R.id.ll_msg_container)
    private val fxChat: FlexBoxChatLayout? = itemView?.findViewById(R.id.fxChat)
    private val onTouchListener = MessageOnTouchListener(msgCliclLinkListener)
    private val headerInfo: LinearLayout? = itemView?.findViewById(R.id.ll_header_info)

    private val headerRole: Typography? = itemView?.findViewById(R.id.tvRole)
    private val smartReplyBlueDot: ImageView? = itemView?.findViewById(R.id.img_sr_blue_dot)
    private val header: LinearLayout? = itemView?.findViewById(R.id.llRoleUser)
    private val bodyMsgContainer: LinearLayout? = itemView?.findViewById(
        R.id.ll_body_msg_container
    )

    private val topMarginOpposite: Float = getOppositeMargin(itemView?.context)
    private val bubbleToScreenMargin: Float = itemView?.context?.resources?.getDimension(
        com.tokopedia.unifyprinciples.R.dimen.unify_space_12
    ) ?: 0f

    private val bgLeft = generateLeftBg(fxChat)
    private val bgRight = generateRightBg(fxChat)

    override fun bind(msg: MessageViewModel, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        when (payloads.first()) {
            Payload.REBIND -> bind(msg)
        }
    }

    override fun bind(msg: MessageViewModel) {
        fxChat?.listener = chatMsgListener
        verifyReplyTime(msg)
        ChatMessageViewHolderBinder.bindChatMessage(msg, fxChat)
        ChatMessageViewHolderBinder.bindOnTouchMessageListener(fxChat, onTouchListener)
        ChatMessageViewHolderBinder.bindHour(msg, fxChat)
        bindReplyBubbleListener()
        bindReplyReference(msg)
        bindAttachment(msg)
        bindMargin(msg)
        bindClick()
        bindLongClick(msg)
        if (msg.isSender) {
            // Right msg
            bindMsgGravity(Gravity.END)
            paddingRightMsg()
            bindBackground(bgRight)
            ChatMessageViewHolderBinder.bindChatReadStatus(msg, fxChat)
            bindHeader(msg)
            hide(fxChat?.info)
            hide(headerInfo)
        } else {
            // Left msg
            bindMsgGravity(Gravity.START)
            paddingLeftMsg()
            bindBackground(bgLeft)
            bindMessageInfo(msg)
            bindHeaderInfo(msg)
            hide(fxChat?.checkMark)
            hide(header)
        }
    }

    private fun bindLongClick(msg: MessageViewModel) {
        if (!msg.isBanned()) {
            fxChat?.setOnLongClickListener {
                commonListener.showMsgMenu(msg, fxChat.message?.text ?: "")
                true
            }
        } else {
            fxChat?.setOnClickListener(null)
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

    private fun bindReplyReference(msg: MessageViewModel) {
        messageBubble?.bindReplyData(msg)
    }

    private fun bindAttachment(msg: MessageViewModel) {
        if (msg.hasAttachment()) {
            val shouldHideDivider = commonListener.isSeller() && msg.isSender
            fxChat?.renderHeaderAttachment(
                msg.attachment,
                shouldHideDivider
            )
        } else {
            fxChat?.hideAttachmentHeader()
        }
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

    private fun bindMessageInfo(msg: MessageViewModel) {
        if (msg.hasLabel()) {
            fxChat?.showInfo(msg.label)
        } else {
            fxChat?.hideInfo()
        }
    }

    private fun verifyReplyTime(chat: MessageViewModel) {
        try {
            if (chat.replyTime.toLongOrZero() / MILISECONDS < START_YEAR) {
                chat.replyTime = (chat.replyTime.toLongOrZero() * MILISECONDS).toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bindMargin(message: MessageViewModel) {
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

    private fun bindBackground(drawable: Drawable?) {
        fxChat?.background = drawable
    }

    private fun bindHeaderInfo(msg: MessageViewModel) {
        if (
            msg.source == BaseChatViewModel.SOURCE_REPLIED_BLAST &&
            commonListener.isSeller()
        ) {
            headerInfo?.show()
        } else {
            headerInfo?.hide()
        }
    }

    private fun bindHeader(message: MessageViewModel) {
        bindHeaderSmartReply(message)
        bindHeaderAutoReply(message)
        bindHeaderVisibility(message)
    }

    private fun bindHeaderSmartReply(message: MessageViewModel) {
        if (fromSmartReply(message)) {
            val headerText = itemView.context?.getString(R.string.tittle_header_smart_reply)
            headerRole?.text = headerText
        }
    }

    private fun bindHeaderAutoReply(message: MessageViewModel) {
        if (fromAutoReply(message)) {
            val headerText = itemView.context?.getString(R.string.tittle_header_auto_reply)
            headerRole?.text = headerText
        }
    }

    private fun bindHeaderVisibility(message: MessageViewModel) {
        if (fromAutoReply(message) || fromSmartReply(message)) {
            bindBlueDot(message)
            header?.show()
            headerRole?.show()
        } else {
            header?.hide()
        }
    }

    private fun fromAutoReply(msg: MessageViewModel): Boolean {
        return msg.isSender && commonListener.isSeller() && msg.isFromAutoReply()
    }

    private fun fromSmartReply(msg: MessageViewModel): Boolean {
        return msg.isSender && commonListener.isSeller() && msg.isFromSmartReply()
    }

    private fun bindBlueDot(message: MessageViewModel) {
        if (message.isFromSmartReply()) {
            smartReplyBlueDot?.show()
        } else {
            smartReplyBlueDot?.hide()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_chat_bubble_unify
        const val TYPE_BANNED = 2
    }
}