package com.tokopedia.topchat.chatroom.view.adapter.util

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import com.tokopedia.topchat.R
import com.tokopedia.topchat.common.util.ViewUtil
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

object TopChatRoomBubbleBackgroundGenerator {

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
            backgroundColor = unifyprinciplesR.color.Unify_GN50,
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
            backgroundColor = unifyprinciplesR.color.Unify_NN0,
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
}
