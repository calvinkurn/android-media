package com.tokopedia.sellerhomecommon.utils

import android.graphics.drawable.ScaleDrawable
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