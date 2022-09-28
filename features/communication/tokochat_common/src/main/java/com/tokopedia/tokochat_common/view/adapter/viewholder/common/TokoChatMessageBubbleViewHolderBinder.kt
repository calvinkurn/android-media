package com.tokopedia.tokochat_common.view.adapter.viewholder.common

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.tokochat_common.util.TokoChatTimeConverter
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.util.TokoChatViewUtil
import com.tokopedia.tokochat_common.util.TokoChatValueUtil.MILLISECONDS
import com.tokopedia.tokochat_common.view.customview.TokoChatMessageChatLayout
import com.tokopedia.tokochat_common.view.uimodel.TokoChatMessageBubbleBaseUiModel
import com.tokopedia.unifycomponents.ImageUnify

object TokoChatMessageBubbleViewHolderBinder {

    fun generateRightBg(
        view: View?,
        usePressedBackground: Boolean = true
    ): Drawable? {
        val pressedBackground = if (usePressedBackground) {
            TokoChatViewUtil.generateBackgroundWithShadow(
                view = view,
                backgroundColor = R.color.tokochat_dms_right_button_pressed,
                topLeftRadius = R.dimen.tokochat_20dp,
                topRightRadius = R.dimen.tokochat_0dp,
                bottomLeftRadius = R.dimen.tokochat_20dp,
                bottomRightRadius = R.dimen.tokochat_20dp,
                shadowColor = R.color.tokochat_dms_chat_bubble_shadow,
                elevation = R.dimen.tokochat_2dp,
                shadowRadius = R.dimen.tokochat_1dp,
                shadowGravity = Gravity.CENTER
            )
        } else {
            null
        }
        return TokoChatViewUtil.generateBackgroundWithShadow(
            view = view,
            backgroundColor = R.color.tokochat_bg_dms_right_bubble,
            topLeftRadius = R.dimen.tokochat_20dp,
            topRightRadius = R.dimen.tokochat_0dp,
            bottomLeftRadius = R.dimen.tokochat_20dp,
            bottomRightRadius = R.dimen.tokochat_20dp,
            shadowColor = R.color.tokochat_dms_chat_bubble_shadow,
            elevation = R.dimen.tokochat_2dp,
            shadowRadius = R.dimen.tokochat_1dp,
            shadowGravity = Gravity.CENTER,
            pressedDrawable = pressedBackground
        )
    }

    fun generateLeftBg(
        view: View?,
        usePressedBackground: Boolean = true
    ): Drawable? {
        val pressedBackground = if (usePressedBackground) {
            TokoChatViewUtil.generateBackgroundWithShadow(
                view = view,
                backgroundColor = R.color.tokochat_dms_left_button_pressed,
                topLeftRadius = R.dimen.tokochat_0dp,
                topRightRadius = R.dimen.tokochat_20dp,
                bottomLeftRadius = R.dimen.tokochat_20dp,
                bottomRightRadius = R.dimen.tokochat_20dp,
                shadowColor = R.color.tokochat_dms_chat_bubble_shadow,
                elevation = R.dimen.tokochat_2dp,
                shadowRadius = R.dimen.tokochat_1dp,
                shadowGravity = Gravity.CENTER
            )
        } else {
            null
        }
        return TokoChatViewUtil.generateBackgroundWithShadow(
            view = view,
            backgroundColor = R.color.tokochat_bg_dms_left_bubble,
            topLeftRadius = R.dimen.tokochat_1dp,
            topRightRadius = R.dimen.tokochat_20dp,
            bottomLeftRadius = R.dimen.tokochat_20dp,
            bottomRightRadius = R.dimen.tokochat_20dp,
            shadowColor = R.color.tokochat_dms_chat_bubble_shadow,
            elevation = R.dimen.tokochat_2dp,
            shadowRadius = R.dimen.tokochat_1dp,
            shadowGravity = Gravity.CENTER,
            pressedDrawable = pressedBackground
        )
    }

    fun bindChatMessage(
        chat: TokoChatMessageBubbleBaseUiModel,
        tokoChatMessageChatLayout: TokoChatMessageChatLayout?
    ) {
        val htmlMessage = MethodChecker.fromHtml(chat.message)
        tokoChatMessageChatLayout?.setMessageTypeFace(chat)
        tokoChatMessageChatLayout?.setMessage(chat, htmlMessage)
    }

    fun bindHour(
        uiModel: TokoChatMessageBubbleBaseUiModel,
        tokoChatMessageChatLayout: TokoChatMessageChatLayout?
    ) {
        val hourTime = getHourTime(uiModel.replyTime)
        tokoChatMessageChatLayout?.setHourTime(hourTime)
    }

    private fun getHourTime(replyTime: String?): String {
        return try {
            replyTime?.let {
                TokoChatTimeConverter.formatTime(replyTime.toLong() / MILLISECONDS)
            } ?: ""
        } catch (e: NumberFormatException) {
            replyTime ?: ""
        }
    }

    fun bindChatReadStatus(
        element: TokoChatMessageBubbleBaseUiModel,
        tokoChatMessageChatLayout: TokoChatMessageChatLayout?
    ) {
        tokoChatMessageChatLayout?.checkMark?.let {
            bindChatReadStatus(element, it)
        }
    }

    private fun bindChatReadStatus(element: TokoChatMessageBubbleBaseUiModel, checkMark: ImageUnify) {
        if (element.isShowTime && element.isSender && !element.isDeleted()) {
            checkMark.show()
            val imageResource = when {
                element.isDummy -> R.drawable.tokochat_ic_check_rounded_grey
                !element.isRead -> R.drawable.tokochat_ic_check_sent_rounded_grey
                else -> R.drawable.tokochat_ic_check_read_rounded_green
            }
            val drawable = MethodChecker.getDrawable(checkMark.context, imageResource)
            checkMark.setImageDrawable(drawable)
        } else {
            checkMark.gone()
        }
    }

}
