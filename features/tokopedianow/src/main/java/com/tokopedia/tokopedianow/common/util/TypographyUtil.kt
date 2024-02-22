package com.tokopedia.tokopedianow.common.util

import android.graphics.drawable.Drawable
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.tokopedia.unifyprinciples.Typography

object TypographyUtil {
    private const val DEFAULT_BOUND = 0

    fun Typography.setRightImageDrawable(
        drawable: Drawable?,
        width: Int,
        height: Int,
        color: Int
    ) {
        if (drawable != null) {
            drawable.setBounds(
                DEFAULT_BOUND,
                DEFAULT_BOUND,
                width,
                height
            )
            drawable.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                color,
                BlendModeCompat.SRC_ATOP
            )
            setCompoundDrawables(null, null, drawable, null)
        }
    }
}
