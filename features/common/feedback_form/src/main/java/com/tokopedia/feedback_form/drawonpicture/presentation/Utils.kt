package com.tokopedia.feedback_form.drawonpicture.presentation

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable

/**
 * @author by furqan on 07/10/2020
 */
object Utils {
    fun changeShapeColor(background: Drawable, color: String) {
        when (background) {
            is ShapeDrawable -> {
                background.paint.color = Color.parseColor(color)
            }
            is GradientDrawable -> {
                background.setColor(Color.parseColor(color))
            }
            is ColorDrawable -> {
                background.color = Color.parseColor(color)
            }
        }
    }
}