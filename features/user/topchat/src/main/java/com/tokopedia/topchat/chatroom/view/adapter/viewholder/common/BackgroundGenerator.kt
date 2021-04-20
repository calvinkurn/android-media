package com.tokopedia.topchat.chatroom.view.adapter.viewholder.common

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import com.tokopedia.topchat.R
import com.tokopedia.topchat.common.util.ViewUtil

object BackgroundGenerator {

    fun generateLeftBackgroundReviewReminder(
            view: View?
    ): Drawable? {
        return ViewUtil.generateBackgroundWithShadow(
                view = view,
                backgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_N0,
                topLeftRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
                topRightRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
                bottomLeftRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
                bottomRightRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
                shadowColor = R.color.topchat_dms_message_shadow,
                elevation = R.dimen.dp_topchat_2,
                shadowRadius = R.dimen.dp_topchat_1,
                shadowGravity = Gravity.CENTER
        )
    }
}