package com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.topchat.R
import com.tokopedia.topchat.common.util.ViewUtil

object ReplyBubbleBinder {
    fun generateRightBg(view: View?): Drawable? {
        return ViewUtil.generateBackgroundWithShadow(
            view = view,
            backgroundColor = R.color.topchat_bg_dms_right_bubble_reply,
            strokeColor = com.tokopedia.unifyprinciples.R.color.Unify_GN50,
            strokeWidth = R.dimen.dp_topchat_2,
            strokePaddingBottom = 27f.toPx().toInt(),
            topLeftRadius = R.dimen.dp_topchat_20,
            topRightRadius = R.dimen.dp_topchat_0,
            bottomLeftRadius = R.dimen.dp_topchat_0,
            bottomRightRadius = R.dimen.dp_topchat_0,
            shadowColor = R.color.topchat_dms_chat_bubble_shadow,
            elevation = R.dimen.dp_topchat_2,
            shadowRadius = R.dimen.dp_topchat_1,
            shadowGravity = Gravity.CENTER
        )
    }

    fun generateLeftBg(view: View?): Drawable? {
        return ViewUtil.generateBackgroundWithShadow(
            view = view,
            backgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_NN50,
            strokeColor = R.color.topchat_bg_dms_left_bubble,
            strokeWidth = R.dimen.dp_topchat_2,
            strokePaddingBottom = 27f.toPx().toInt(),
            topLeftRadius = R.dimen.dp_topchat_0,
            topRightRadius = R.dimen.dp_topchat_20,
            bottomLeftRadius = R.dimen.dp_topchat_0,
            bottomRightRadius = R.dimen.dp_topchat_0,
            shadowColor = R.color.topchat_dms_chat_bubble_shadow,
            elevation = R.dimen.dp_topchat_2,
            shadowRadius = R.dimen.dp_topchat_1,
            shadowGravity = Gravity.CENTER
        )
    }
}
