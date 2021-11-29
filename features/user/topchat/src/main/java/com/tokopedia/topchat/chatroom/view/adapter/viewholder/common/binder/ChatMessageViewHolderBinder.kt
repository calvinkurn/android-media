package com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.util.ChatTimeConverter
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.util.MessageOnTouchListener
import com.tokopedia.topchat.chatroom.view.custom.FlexBoxChatLayout
import com.tokopedia.topchat.common.util.ViewUtil

object ChatMessageViewHolderBinder {

    fun generateRightBg(
        view: View?,
        usePressedBackground: Boolean = true
    ): Drawable? {
        val pressedBackground = if (usePressedBackground) {
            ViewUtil.generateBackgroundWithShadow(
                view = view,
                backgroundColor = R.color.topchat_dms_right_button_pressed,
                topLeftRadius = R.dimen.dp_topchat_20,
                topRightRadius = R.dimen.dp_topchat_0,
                bottomLeftRadius = R.dimen.dp_topchat_20,
                bottomRightRadius = R.dimen.dp_topchat_20,
                shadowColor = R.color.topchat_dms_chat_bubble_shadow,
                elevation = R.dimen.dp_topchat_2,
                shadowRadius = R.dimen.dp_topchat_1,
                shadowGravity = Gravity.CENTER
            )
        } else {
            null
        }
        return ViewUtil.generateBackgroundWithShadow(
            view = view,
            backgroundColor = R.color.topchat_bg_dms_right_bubble,
            topLeftRadius = R.dimen.dp_topchat_20,
            topRightRadius = R.dimen.dp_topchat_0,
            bottomLeftRadius = R.dimen.dp_topchat_20,
            bottomRightRadius = R.dimen.dp_topchat_20,
            shadowColor = R.color.topchat_dms_chat_bubble_shadow,
            elevation = R.dimen.dp_topchat_2,
            shadowRadius = R.dimen.dp_topchat_1,
            shadowGravity = Gravity.CENTER,
            pressedDrawable = pressedBackground
        )
    }

    fun generateLeftBg(
        view: View?,
        usePressedBackground: Boolean = true
    ): Drawable? {
        val pressedBackground = if (usePressedBackground) {
            ViewUtil.generateBackgroundWithShadow(
                view = view,
                backgroundColor = R.color.topchat_dms_left_button_pressed,
                topLeftRadius = R.dimen.dp_topchat_0,
                topRightRadius = R.dimen.dp_topchat_20,
                bottomLeftRadius = R.dimen.dp_topchat_20,
                bottomRightRadius = R.dimen.dp_topchat_20,
                shadowColor = R.color.topchat_dms_chat_bubble_shadow,
                elevation = R.dimen.dp_topchat_2,
                shadowRadius = R.dimen.dp_topchat_1,
                shadowGravity = Gravity.CENTER
            )
        } else {
            null
        }
        return ViewUtil.generateBackgroundWithShadow(
            view = view,
            backgroundColor = R.color.topchat_bg_dms_left_bubble,
            topLeftRadius = R.dimen.dp_topchat_0,
            topRightRadius = R.dimen.dp_topchat_20,
            bottomLeftRadius = R.dimen.dp_topchat_20,
            bottomRightRadius = R.dimen.dp_topchat_20,
            shadowColor = R.color.topchat_dms_chat_bubble_shadow,
            elevation = R.dimen.dp_topchat_2,
            shadowRadius = R.dimen.dp_topchat_1,
            shadowGravity = Gravity.CENTER,
            pressedDrawable = pressedBackground
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    fun bindChatMessage(
        chat: MessageUiModel,
        fxChat: FlexBoxChatLayout?
    ) {
        val htmlMessage = MethodChecker.fromHtml(chat.message)
        fxChat?.setMessage(htmlMessage)
    }

    fun bindOnTouchMessageListener(
        fxChat: FlexBoxChatLayout?,
        onTouchListener: MessageOnTouchListener
    ) {
        fxChat?.setMessageOnTouchListener(onTouchListener)
    }

    fun bindHour(
        uiModel: MessageUiModel,
        fxChat: FlexBoxChatLayout?
    ) {
        val hourTime = getHourTime(uiModel.replyTime)
        fxChat?.setHourTime(hourTime)
    }

    fun bindHourTextView(
        uiModel: MessageUiModel,
        hour: TextView?
    ) {
        val hourTime = getHourTime(uiModel.replyTime)
        hour?.text = hourTime
    }

    private fun getHourTime(replyTime: String?): String {
        return try {
            replyTime?.let {
                ChatTimeConverter.formatTime(replyTime.toLong() / BaseChatViewHolder.MILISECONDS)
            } ?: ""
        } catch (e: NumberFormatException) {
            replyTime ?: ""
        }
    }

    fun bindChatReadStatus(element: MessageUiModel, messageView: FlexBoxChatLayout?) {
        messageView?.checkMark?.let {
            bindChatReadStatus(element, it)
        }
    }

    fun bindChatReadStatus(element: MessageUiModel, checkMark: ImageView) {
        if (element.isShowTime && element.isSender) {
            checkMark.show()
            val imageResource = when {
                element.isDummy -> com.tokopedia.chat_common.R.drawable.ic_chatcommon_check_rounded_grey
                !element.isRead -> com.tokopedia.chat_common.R.drawable.ic_chatcommon_check_sent_rounded_grey
                else -> com.tokopedia.chat_common.R.drawable.ic_chatcommon_check_read_rounded_green
            }
            val drawable = MethodChecker.getDrawable(checkMark.context, imageResource)
            checkMark.setImageDrawable(drawable)
        } else {
            checkMark.gone()
        }
    }

}