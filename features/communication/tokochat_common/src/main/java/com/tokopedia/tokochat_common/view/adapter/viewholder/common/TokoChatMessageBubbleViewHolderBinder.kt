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
import com.tokopedia.tokochat_common.util.TokoChatViewUtil.ONE_DP
import com.tokopedia.tokochat_common.util.TokoChatViewUtil.TWENTY_DP
import com.tokopedia.tokochat_common.util.TokoChatViewUtil.TWO_DP
import com.tokopedia.tokochat_common.util.TokoChatViewUtil.ZERO_DP
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
                topLeftRadiusValue = TWENTY_DP,
                topRightRadiusValue = ZERO_DP,
                bottomLeftRadiusValue = TWENTY_DP,
                bottomRightRadiusValue = TWENTY_DP,
                shadowColor = com.tokopedia.unifyprinciples.R.color.Unify_Static_Black_20,
                elevationValue = TWO_DP,
                shadowRadiusValue = ONE_DP,
                shadowGravity = Gravity.CENTER
            )
        } else {
            null
        }
        return TokoChatViewUtil.generateBackgroundWithShadow(
            view = view,
            backgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_GN50,
            topLeftRadiusValue = TWENTY_DP,
            topRightRadiusValue = ZERO_DP,
            bottomLeftRadiusValue = TWENTY_DP,
            bottomRightRadiusValue = TWENTY_DP,
            shadowColor = com.tokopedia.unifyprinciples.R.color.Unify_Static_Black_20,
            elevationValue = TWO_DP,
            shadowRadiusValue = ONE_DP,
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
                topLeftRadiusValue = ZERO_DP,
                topRightRadiusValue = TWENTY_DP,
                bottomLeftRadiusValue = TWENTY_DP,
                bottomRightRadiusValue = TWENTY_DP,
                shadowColor = com.tokopedia.unifyprinciples.R.color.Unify_Static_Black_20,
                elevationValue = TWO_DP,
                shadowRadiusValue = ONE_DP,
                shadowGravity = Gravity.CENTER
            )
        } else {
            null
        }
        return TokoChatViewUtil.generateBackgroundWithShadow(
            view = view,
            backgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_NN0,
            topLeftRadiusValue = ONE_DP,
            topRightRadiusValue = TWENTY_DP,
            bottomLeftRadiusValue = TWENTY_DP,
            bottomRightRadiusValue = TWENTY_DP,
            shadowColor = com.tokopedia.unifyprinciples.R.color.Unify_Static_Black_20,
            elevationValue = TWO_DP,
            shadowRadiusValue = ONE_DP,
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
