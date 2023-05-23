package com.tokopedia.sellerhomecommon.utils

import android.graphics.drawable.ScaleDrawable
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.getResColor

/**
 * Created By @ilhamsuaib on 18/10/20
 */

internal const val DP_16 = 16

fun TextView.setUnifyDrawableEnd(
    iconId: Int,
    colorIcon: Int = context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_NN600),
    width: Float = context.dpToPx(DP_16),
    height: Float = context.dpToPx(DP_16)
) {
    val icon = getIconUnifyDrawable(context, iconId, colorIcon)
    val drawable = ScaleDrawable(icon, Int.ZERO, width, height).drawable
    drawable?.setBounds(Int.ZERO, Int.ZERO, width.toInt(), width.toInt())
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
            Int.ZERO
        }
    requestLayout()
}