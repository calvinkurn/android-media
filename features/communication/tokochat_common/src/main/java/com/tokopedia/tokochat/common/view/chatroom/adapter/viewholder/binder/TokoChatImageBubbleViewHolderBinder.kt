package com.tokopedia.tokochat.common.view.chatroom.adapter.viewholder.binder

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import com.tokopedia.tokochat.common.util.TokoChatViewUtil
import com.tokopedia.tokochat.common.util.TokoChatViewUtil.EIGHT_DP
import com.tokopedia.tokochat.common.util.TokoChatViewUtil.ONE_DP
import com.tokopedia.tokochat.common.util.TokoChatViewUtil.THIRTY_EIGHT_DP
import com.tokopedia.tokochat.common.util.TokoChatViewUtil.THREE_DP
import com.tokopedia.tokochat.common.util.TokoChatViewUtil.TWO_DP

object TokoChatImageBubbleViewHolderBinder {

    fun generateLeftBg(view: View?): Drawable? {
        return TokoChatViewUtil.generateBackgroundWithShadow(
            view = view,
            backgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_NN0,
            topLeftRadiusValue = EIGHT_DP,
            topRightRadiusValue = EIGHT_DP,
            bottomLeftRadiusValue = EIGHT_DP,
            bottomRightRadiusValue = EIGHT_DP,
            shadowColor = com.tokopedia.unifyprinciples.R.color.Unify_NN950_20,
            elevationValue = TWO_DP,
            shadowRadiusValue = ONE_DP,
            shadowGravity = Gravity.CENTER,
            strokeColor = com.tokopedia.unifyprinciples.R.color.Unify_Background,
            strokeWidthValue = THREE_DP
        )
    }

    fun generateRightBg(view: View?): Drawable? {
        return TokoChatViewUtil.generateBackgroundWithShadow(
            view = view,
            backgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_GN50,
            topLeftRadiusValue = EIGHT_DP,
            topRightRadiusValue = EIGHT_DP,
            bottomLeftRadiusValue = EIGHT_DP,
            bottomRightRadiusValue = EIGHT_DP,
            shadowColor = com.tokopedia.unifyprinciples.R.color.Unify_NN950_20,
            elevationValue = TWO_DP,
            shadowRadiusValue = ONE_DP,
            shadowGravity = Gravity.CENTER,
            strokeColor = com.tokopedia.unifyprinciples.R.color.Unify_GN50,
            strokeWidthValue = THREE_DP
        )
    }

    fun generateTextButtonBg(view: View?): Drawable? {
        return TokoChatViewUtil.generateBackgroundWithShadow(
            view = view,
            backgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_NN0,
            topLeftRadiusValue = THIRTY_EIGHT_DP,
            topRightRadiusValue = THIRTY_EIGHT_DP,
            bottomLeftRadiusValue = THIRTY_EIGHT_DP,
            bottomRightRadiusValue = THIRTY_EIGHT_DP,
            shadowColor = com.tokopedia.unifyprinciples.R.color.Unify_NN950_20,
            elevationValue = TWO_DP,
            shadowRadiusValue = ONE_DP,
            shadowGravity = Gravity.CENTER,
            strokeColor = com.tokopedia.unifyprinciples.R.color.Unify_Background,
            strokeWidthValue = THREE_DP,
            useViewPadding = true
        )
    }
}
