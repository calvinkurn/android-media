package com.tokopedia.flight.orderdetail.presentation.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import androidx.annotation.ColorRes
import com.tokopedia.abstraction.common.utils.view.MethodChecker

/**
 * @author by furqan on 26/10/2020
 */
object OrderDetailUtils {
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

    fun changeShapeColor(context: Context, background: Drawable, @ColorRes color: Int) {
        when (background) {
            is ShapeDrawable -> {
                background.paint.color = MethodChecker.getColor(context, color)
            }
            is GradientDrawable -> {
                background.setColor(MethodChecker.getColor(context, color))
            }
            is ColorDrawable -> {
                background.color = MethodChecker.getColor(context, color)
            }
        }
    }
}