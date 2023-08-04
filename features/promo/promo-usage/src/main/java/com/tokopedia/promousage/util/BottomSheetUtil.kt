package com.tokopedia.promousage.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.tokopedia.promousage.R

internal object BottomSheetUtil {

    fun generateBackgroundDrawableWithColor(context: Context, hexColor: String): Drawable? {
        val bgDrawable = ContextCompat
            .getDrawable(context, R.drawable.bottomsheet_background)
        bgDrawable?.let {
            DrawableCompat.setTint(DrawableCompat.wrap(it), Color.parseColor(hexColor))
        }
        return bgDrawable
    }
}
