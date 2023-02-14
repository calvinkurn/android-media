package com.tokopedia.tokochat_common.view.adapter.viewholder.binder

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import com.tokopedia.tokochat_common.util.TokoChatViewUtil
import com.tokopedia.tokochat_common.util.TokoChatViewUtil.EIGHT_DP
import com.tokopedia.tokochat_common.util.TokoChatViewUtil.ONE_DP
import com.tokopedia.tokochat_common.util.TokoChatViewUtil.THREE_DP
import com.tokopedia.tokochat_common.util.TokoChatViewUtil.TWO_DP

object TokoChatImageBubbleViewHolderBinder {

    fun generateLeftBg(view: View?): Drawable? {
        return TokoChatViewUtil.generateBackgroundWithShadow(
            view = view,
            backgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_Background,
            topLeftRadiusValue = EIGHT_DP,
            topRightRadiusValue = EIGHT_DP,
            bottomLeftRadiusValue = EIGHT_DP,
            bottomRightRadiusValue = EIGHT_DP,
            shadowColor = com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
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
            shadowColor = com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            elevationValue = TWO_DP,
            shadowRadiusValue = ONE_DP,
            shadowGravity = Gravity.CENTER,
            strokeColor = com.tokopedia.unifyprinciples.R.color.Unify_GN50,
            strokeWidthValue = THREE_DP
        )
    }
}
