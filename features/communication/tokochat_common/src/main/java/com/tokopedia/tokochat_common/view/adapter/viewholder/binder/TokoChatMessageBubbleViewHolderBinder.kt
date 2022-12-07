package com.tokopedia.tokochat_common.view.adapter.viewholder.binder

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.tokochat_common.util.TokoChatTimeConverter
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.util.TokoChatViewUtil
import com.tokopedia.tokochat_common.util.TokoChatViewUtil.ONE_DP
import com.tokopedia.tokochat_common.util.TokoChatViewUtil.TWENTY_DP
import com.tokopedia.tokochat_common.util.TokoChatViewUtil.TWO_DP
import com.tokopedia.tokochat_common.util.TokoChatViewUtil.ZERO_DP
import com.tokopedia.tokochat_common.view.customview.TokoChatMessageChatLayout
import com.tokopedia.tokochat_common.view.uimodel.TokoChatMessageBubbleUiModel
import com.tokopedia.tokochat_common.view.uimodel.base.TokoChatSendableBaseUiModel
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
        chat: TokoChatMessageBubbleUiModel,
        tokoChatMessageChatLayout: TokoChatMessageChatLayout?
    ) {
        val htmlMessage = MethodChecker.fromHtml(chat.messageText)
        tokoChatMessageChatLayout?.setMessageTypeFace(chat)
        tokoChatMessageChatLayout?.setMessage(htmlMessage)
    }

    fun bindHour(
        uiModel: TokoChatMessageBubbleUiModel,
        tokoChatMessageChatLayout: TokoChatMessageChatLayout?
    ) {
        val hourTime = getHourTime(uiModel.messageTime)
        tokoChatMessageChatLayout?.setHourTime(hourTime)
    }

    private fun getHourTime(replyTime: Long): String {
        return try {
            TokoChatTimeConverter.formatTime(replyTime)
        } catch (error: Throwable) {
            ""
        }
    }

    fun bindChatReadStatus(
        element: TokoChatMessageBubbleUiModel,
        tokoChatMessageChatLayout: TokoChatMessageChatLayout?
    ) {
        tokoChatMessageChatLayout?.checkMark?.let {
            bindChatReadStatus(element, it)
        }
    }

    fun bindChatReadStatus(element: TokoChatSendableBaseUiModel, checkMark: ImageUnify) {
        if (element.isSender) {
            checkMark.show()
            val imageResource = when {
                element.isRead() -> R.drawable.tokochat_ic_check_read_rounded_green
                element.isSent() -> R.drawable.tokochat_ic_check_sent_rounded_grey
                else -> R.drawable.tokochat_ic_check_rounded_grey
            }
            val drawable = MethodChecker.getDrawable(checkMark.context, imageResource)
            checkMark.setImageDrawable(drawable)
        } else {
            checkMark.gone()
        }
    }
}
