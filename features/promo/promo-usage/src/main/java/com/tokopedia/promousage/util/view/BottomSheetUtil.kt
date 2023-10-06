package com.tokopedia.promousage.util.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.tokopedia.unifycomponents.R as unifycomponentsR

internal object BottomSheetUtil {

    fun generateBackgroundDrawableWithColor(context: Context, hexColor: String): Drawable? {
        val bgDrawable = ContextCompat
            .getDrawable(context, unifycomponentsR.drawable.bottomsheet_background)
        bgDrawable?.let {
            if (hexColor.isNotBlank()) {
                DrawableCompat.setTint(DrawableCompat.wrap(it), Color.parseColor(hexColor))
            } else {
                DrawableCompat.setTint(
                    DrawableCompat.wrap(it),
                    ContextCompat.getColor(
                        context,
                        unifycomponentsR.color.Unify_Background
                    )
                )
            }
        }
        return bgDrawable
    }

    fun generateBackgroundDrawableWithColor(context: Context, @ColorRes colorRes: Int): Drawable? {
        val bgDrawable = ContextCompat
            .getDrawable(context, unifycomponentsR.drawable.bottomsheet_background)
        bgDrawable?.let {
            DrawableCompat.setTint(
                DrawableCompat.wrap(it),
                ContextCompat.getColor(context, colorRes)
            )
        }
        return bgDrawable
    }
}
