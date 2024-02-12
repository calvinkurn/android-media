package com.tokopedia.topchat.chatroom.view.adapter.viewholder.messagebubble.base

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.util.ChatTimeConverter.getHourTime
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.util.LongClickMenuItemGenerator
import com.tokopedia.topchat.chatroom.view.adapter.util.MessageOnTouchListener
import com.tokopedia.topchat.chatroom.view.adapter.util.TopChatRoomBubbleBackgroundGenerator.generateLeftBg
import com.tokopedia.topchat.chatroom.view.adapter.util.TopChatRoomBubbleBackgroundGenerator.generateRightBg
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.Payload
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.getOppositeMargin
import com.tokopedia.topchat.chatroom.view.custom.message.ReplyBubbleAreaMessage
import com.tokopedia.topchat.chatroom.view.custom.messagebubble.base.BaseTopChatFlexBoxChatLayout
import com.tokopedia.topchat.chatroom.view.custom.messagebubble.base.BaseTopChatRoomMessageBubbleLayout
import com.tokopedia.topchat.chatroom.view.custom.messagebubble.base.TopChatRoomBubbleContainerLayout
import com.tokopedia.topchat.chatroom.view.custom.messagebubble.base.TopChatRoomFlexBoxListener
import com.tokopedia.unifyprinciples.Typography
import timber.log.Timber

abstract class BaseTopChatBubbleMessageViewHolder<T : MessageUiModel>(
    itemView: View,
    msgClickLinkListener: ChatLinkHandlerListener,
    private val commonListener: CommonViewHolderListener,
    private val adapterListener: AdapterListener,
    private val chatMsgListener: TopChatRoomFlexBoxListener,
    private val replyBubbleListener: ReplyBubbleAreaMessage.Listener
) : BaseChatViewHolder<T>(itemView) {

    private val onTouchListener = MessageOnTouchListener(msgClickLinkListener)

    private val headerRole: Typography? = itemView.findViewById(R.id.tvRole)
    private val smartReplyBlueDot: ImageView? = itemView.findViewById(
        R.id.topchat_chatroom_iv_header_role_blue_dot
    )
    private val header: LinearLayout? = itemView.findViewById(R.id.llRoleUser)

    private val topMarginOpposite: Int = getOppositeMargin(itemView.context)
    private val bubbleToScreenMargin: Int = 12.dpToPx(itemView.resources.displayMetrics)

    private var bgLeft: Drawable? = null
    private var bgRight: Drawable? = null

    abstract fun getBubbleChatLayout(): TopChatRoomBubbleContainerLayout?
    abstract fun getLayoutContainerBubble(): LinearLayout?
    abstract fun getMessageBubbleLayout(): BaseTopChatRoomMessageBubbleLayout?
    abstract fun getFxChat(): BaseTopChatFlexBoxChatLayout?
    abstract fun getHeaderInfo(): LinearLayout?

    override fun bind(element: T, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        when (payloads.first()) {
            Payload.REBIND -> bind(element)
        }
    }

    override fun bind(uiModel: T) {
        super.bind(uiModel)
        generateBackground()
        bindListeners(uiModel)
        verifyReplyTime(uiModel)
        bindChatMessage(uiModel)
        bindTextColor(uiModel)
        bindIcon(uiModel)
        bindHour(uiModel)
        bindReplyReference(uiModel)
        bindHeaderAttachment(uiModel)
        bindMargin(uiModel)
        if (uiModel.isSender) {
            // Right msg
            bindSenderBackground(uiModel)
        } else {
            // Left msg
            bindReceiverBackground(uiModel)
        }
    }

    /**
     * Init background drawable
     */
    private fun generateBackground() {
        bgLeft = generateLeftBg(getFxChat())
        bgRight = generateRightBg(getFxChat())
    }

    /**
     * Init Listeners
     */
    private fun bindListeners(uiModel: T) {
        getFxChat()?.setListener(chatMsgListener)
        getFxChat()?.setMessageOnTouchListener(onTouchListener)
        getMessageBubbleLayout()?.setReplyListener(replyBubbleListener)
        itemView.setOnClickListener {
            KeyboardHandler.DropKeyboard(itemView.context, it)
        }
        bindLongClick(uiModel)
    }

    private fun bindLongClick(uiModel: T) {
        if (!uiModel.isBanned() && !uiModel.isDeleted()) {
            getFxChat()?.setOnLongClickListener {
                val menus = LongClickMenuItemGenerator.createLongClickMenuMsgBubble()
                commonListener.showMsgMenu(
                    uiModel,
                    getFxChat()?.getMessageText() ?: "",
                    menus
                )
                true
            }
        } else {
            getFxChat()?.setOnLongClickListener(null)
        }
    }

    private fun verifyReplyTime(chat: T) {
        try {
            if (chat.replyTime.toLongOrZero() / MILISECONDS < START_YEAR) {
                chat.replyTime = (chat.replyTime.toLongOrZero() * MILISECONDS).toString()
            }
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }

    /**
     * Set Message
     */
    private fun bindChatMessage(chat: T) {
        getFxChat()?.setMessageBody(chat)
    }

    private fun bindTextColor(msg: T) {
        getFxChat()?.bindTextColor(msg)
    }

    private fun bindIcon(msg: T) {
        getFxChat()?.bindIcon(msg)
    }

    /**
     * Set Time
     */
    private fun bindHour(uiModel: T) {
        val hourTime = getHourTime(uiModel.replyTime)
        getFxChat()?.setHourTime(hourTime)
    }

    /**
     * Set Reply Bubble Reference
     */
    private fun bindReplyReference(msg: T) {
        getMessageBubbleLayout()?.bindReplyData(msg)
    }

    /**
     * Set header attachment
     */
    private fun bindHeaderAttachment(msg: MessageUiModel) {
        if (msg.hasAttachment()) {
            val shouldHideDivider = commonListener.isSeller() && msg.isSender
            getFxChat()?.renderHeaderAttachment(
                msg.attachment,
                shouldHideDivider
            )
        } else {
            getFxChat()?.hideAttachmentHeader()
        }
    }

    /**
     * Set Bubble Chat Layout (outermost layer)
     */
    private fun bindMargin(message: T) {
        if (adapterListener.isOpposite(absoluteAdapterPosition, message.isSender)) {
            getBubbleChatLayout()?.setMargin(0, topMarginOpposite, 0, 0)
        } else {
            getBubbleChatLayout()?.setMargin(0, 0, 0, 0)
        }
    }

    private fun bindSenderBackground(uiModel: T) {
        bindMsgGravity(Gravity.END)
        paddingRightMsg()
        bindBackground(bgRight)
        bindChatReadStatus(uiModel)
        hideMessageInfo()
        bindHeader(uiModel)
        getHeaderInfo()?.gone()
    }

    private fun bindReceiverBackground(uiModel: T) {
        bindMsgGravity(Gravity.START)
        paddingLeftMsg()
        bindBackground(bgLeft)
        hideReadStatus()
        bindMessageInfo(uiModel)
        header?.gone()
        bindHeaderInfo(uiModel)
    }

    private fun bindMsgGravity(gravity: Int) {
        bindLayoutGravity(gravity)
        bindGravity(gravity)
        bindLayoutMsgGravity(gravity)
        getMessageBubbleLayout()?.setMsgGravity(gravity)
    }

    private fun bindLayoutGravity(gravity: Int) {
        val containerLp = getBubbleChatLayout()?.layoutParams as FrameLayout.LayoutParams
        containerLp.gravity = gravity
        getBubbleChatLayout()?.layoutParams = containerLp
    }

    private fun bindGravity(gravity: Int) {
        getBubbleChatLayout()?.gravity = gravity
    }

    private fun bindLayoutMsgGravity(gravity: Int) {
        getLayoutContainerBubble()?.gravity = gravity
    }

    private fun paddingRightMsg() {
        getBubbleChatLayout()?.let {
            it.setPadding(
                0,
                it.paddingTop,
                bubbleToScreenMargin,
                it.paddingBottom
            )
        }
    }

    private fun paddingLeftMsg() {
        getBubbleChatLayout()?.let {
            it.setPadding(
                bubbleToScreenMargin,
                it.paddingTop,
                0,
                it.paddingBottom
            )
        }
    }

    private fun bindBackground(drawable: Drawable?) {
        getFxChat()?.background = drawable
    }

    private fun bindChatReadStatus(element: T) {
        getFxChat()?.bindChatReadStatus(element)
    }

    private fun hideReadStatus() {
        getFxChat()?.hideReadStatus()
    }

    private fun bindMessageInfo(msg: MessageUiModel) {
        if (msg.hasLabel()) {
            getFxChat()?.showInfo(msg.label)
        } else {
            hideMessageInfo()
        }
    }

    private fun hideMessageInfo() {
        getFxChat()?.hideInfo()
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

    private fun fromSmartReply(msg: MessageUiModel): Boolean {
        return msg.isSender && commonListener.isSeller() && msg.isFromSmartReply()
    }

    private fun bindHeaderAutoReply(message: MessageUiModel) {
        if (fromAutoReply(message)) {
            val headerText = itemView.context?.getString(R.string.tittle_header_auto_reply)
            headerRole?.text = headerText
        }
    }

    private fun fromAutoReply(msg: MessageUiModel): Boolean {
        return msg.isSender && commonListener.isSeller() && msg.isFromAutoReply()
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

    private fun bindBlueDot(message: MessageUiModel) {
        if (message.isFromSmartReply()) {
            smartReplyBlueDot?.show()
        } else {
            smartReplyBlueDot?.hide()
        }
    }

    private fun bindHeaderInfo(msg: MessageUiModel) {
        if (
            msg.source == BaseChatUiModel.SOURCE_REPLIED_BLAST &&
            commonListener.isSeller()
        ) {
            getHeaderInfo()?.show()
        } else {
            getHeaderInfo()?.hide()
        }
    }
}
