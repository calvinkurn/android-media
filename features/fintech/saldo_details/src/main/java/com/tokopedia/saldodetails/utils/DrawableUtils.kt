package com.tokopedia.saldodetails.utils

import android.graphics.drawable.ScaleDrawable
import android.widget.TextView
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.unifyprinciples.R

object DrawableUtils {
    fun TextView.setUnifyDrawableEnd(iconId: Int) {
        val icon = getIconUnifyDrawable(context, iconId, context.getResColor(R.color.Unify_N500))
        val dp16 = context.dpToPx(16)
        val drawable = ScaleDrawable(icon, 0, dp16, dp16).drawable
        drawable?.setBounds(0, 0, dp16.toInt(), dp16.toInt())
        this.setCompoundDrawables(null, null, drawable, null)
    }

    fun TextView.setUnifyDrawableStart(iconId: Int, drawablePadding: Int = 16) {
        val icon = getIconUnifyDrawable(context, iconId, context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_N500))
        val padding = context.dpToPx(drawablePadding)
        val drawable = ScaleDrawable(icon, 0, padding, padding).drawable
        drawable?.setBounds(0, 0, padding.toInt(), padding.toInt())
        this.setCompoundDrawables(drawable, null, null, null)
    }
}