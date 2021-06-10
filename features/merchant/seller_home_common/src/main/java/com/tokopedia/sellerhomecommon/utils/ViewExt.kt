package com.tokopedia.sellerhomecommon.utils

import android.graphics.drawable.ScaleDrawable
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.sellerhomecommon.R

/**
 * Created By @ilhamsuaib on 18/10/20
 */

fun TextView.setUnifyDrawableEnd(
        iconId: Int,
        colorIcon: Int = context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_N500),
        width: Float = context.dpToPx(16),
        height: Float = context.dpToPx(16)
) {
    val icon = getIconUnifyDrawable(context, iconId, colorIcon)
    val drawable = ScaleDrawable(icon, 0, width, height).drawable
    drawable?.setBounds(0, 0, width.toInt(), width.toInt())
    this.setCompoundDrawables(null, null, drawable, null)
}

fun TextView.clearUnifyDrawableEnd() {
    this.setCompoundDrawables(null, null, null, null)
}

/**
 * Toggle height of widget to wrap content or 0.
 * This is used for statistic page because we need to implement widget removal effect
 * without actually removing the widget in the adapter data list.
 * If you need to use this, please make sure that the widget view is wrapped in CardUnify
 */
internal fun View.toggleWidgetHeight(isShown: Boolean) {
    layoutParams.height =
            if (isShown) {
                FrameLayout.LayoutParams.WRAP_CONTENT
            } else {
                0
            }
    requestLayout()
}