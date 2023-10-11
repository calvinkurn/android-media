package com.tokopedia.topchat.chatroom.view.adapter.viewholder.messagebubble.base

import android.graphics.drawable.Drawable
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.util.ChatTimeConverter.getHourTime
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.topchat.chatroom.view.adapter.util.LongClickMenuItemGenerator
import com.tokopedia.topchat.chatroom.view.adapter.util.MessageOnTouchListener
import com.tokopedia.topchat.chatroom.view.adapter.util.TopChatChatRoomBubbleBackgroundGenerator.generateLeftBg
import com.tokopedia.topchat.chatroom.view.adapter.util.TopChatChatRoomBubbleBackgroundGenerator.generateRightBg
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.getOppositeMargin
import com.tokopedia.topchat.chatroom.view.custom.messagebubble.base.BaseTopChatFlexBoxChatLayout
import com.tokopedia.topchat.chatroom.view.custom.messagebubble.base.TopChatFlexBoxListener

abstract class BaseTopChatBubbleMessageViewHolder<T: Visitable<*>>(
    itemView: View,
    private val commonListener: CommonViewHolderListener,
): BaseChatViewHolder<T>(itemView) {

    protected val topMarginOpposite: Int = getOppositeMargin(itemView.context)
    protected val bubbleToScreenMargin: Int = 12.dpToPx(itemView.resources.displayMetrics)

    protected var bgLeft: Drawable? = null
    protected var bgRight: Drawable? = null

    abstract fun getFxChat(): BaseTopChatFlexBoxChatLayout?

    override fun bind(uiModel: T) {
        super.bind(uiModel)
        generateBackground()
    }

    private fun generateBackground() {
        bgLeft = generateLeftBg(getFxChat())
        bgRight = generateRightBg(getFxChat())
    }

    fun bindChatMessage(chat: MessageUiModel) {
        getFxChat()?.setMessageBody(chat)
    }

    fun bindListener(
        listener: TopChatFlexBoxListener
    ) {
        getFxChat()?.setListener(listener)
    }

    fun bindOnTouchMessageListener(
        onTouchListener: MessageOnTouchListener
    ) {
        getFxChat()?.setMessageOnTouchListener(onTouchListener)
    }

    fun bindHour(
        uiModel: MessageUiModel,
    ) {
        val hourTime = getHourTime(uiModel.replyTime)
        getFxChat()?.setHourTime(hourTime)
    }

    fun bindChatReadStatus(element: MessageUiModel) {
        getFxChat()?.bindChatReadStatus(element)
    }

    fun hideReadStatus() {
        getFxChat()?.hideReadStatus()
    }

    fun bindMessageInfo(msg: MessageUiModel) {
        if (msg.hasLabel()) {
            getFxChat()?.showInfo(msg.label)
        } else {
            hideMessageInfo()
        }
    }

    fun hideMessageInfo() {
        getFxChat()?.hideInfo()
    }

    fun bindBackground(drawable: Drawable?) {
        getFxChat()?.background = drawable
    }

    fun bindTextColor(msg: MessageUiModel) {
        getFxChat()?.bindTextColor(msg)
    }

    fun bindIcon(msg: MessageUiModel) {
        getFxChat()?.bindIcon(msg)
    }

    fun bindLongClick(msg: MessageUiModel) {
        if (!msg.isBanned() && !msg.isDeleted()) {
            getFxChat()?.setOnLongClickListener {
                val menus = LongClickMenuItemGenerator.createLongClickMenuMsgBubble()
                commonListener.showMsgMenu(
                    msg, getFxChat()?.getMessageText() ?: "", menus
                )
                true
            }
        } else {
            getFxChat()?.setOnLongClickListener(null)
        }
    }

    fun bindHeaderAttachment(msg: MessageUiModel) {
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
}
