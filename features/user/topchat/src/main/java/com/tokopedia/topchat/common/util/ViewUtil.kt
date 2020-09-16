package com.tokopedia.topchat.common.util

import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.view.Gravity
import android.view.View
import android.view.View.LAYER_TYPE_SOFTWARE
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat


object ViewUtil {
    fun generateBackgroundWithShadow(
            view: View,
            @ColorRes backgroundColor: Int,
            @DimenRes topLeftRadius: Int,
            @DimenRes topRightRadius: Int,
            @DimenRes bottomLeftRadius: Int,
            @DimenRes bottomRightRadius: Int,
            @ColorRes shadowColor: Int,
            @DimenRes elevation: Int,
            @DimenRes shadowRadius: Int,
            shadowGravity: Int
    ): Drawable {
        val topLeftRadiusValue = view.context.resources.getDimension(topLeftRadius)
        val topRightRadiusValue = view.context.resources.getDimension(topRightRadius)
        val bottomLeftRadiusValue = view.context.resources.getDimension(bottomLeftRadius)
        val bottomRightRadiusValue = view.context.resources.getDimension(bottomRightRadius)
        val elevationValue = view.context.resources.getDimension(elevation).toInt()
        val shadowRadiusValue = view.context.resources.getDimension(shadowRadius)
        val shadowColorValue = ContextCompat.getColor(view.context, shadowColor)
        val backgroundColorValue = ContextCompat.getColor(view.context, backgroundColor)

        val outerRadius = floatArrayOf(
                topLeftRadiusValue, topLeftRadiusValue, topRightRadiusValue, topRightRadiusValue,
                bottomLeftRadiusValue, bottomLeftRadiusValue, bottomRightRadiusValue, bottomRightRadiusValue
        )

        val backgroundPaint = Paint()
        backgroundPaint.style = Paint.Style.FILL
        backgroundPaint.setShadowLayer(shadowRadiusValue, 0f, 0f, 0)

        val shapeDrawablePadding = Rect()
        shapeDrawablePadding.left = elevationValue
        shapeDrawablePadding.right = elevationValue

        val DY: Int
        when (shadowGravity) {
            Gravity.CENTER -> {
                shapeDrawablePadding.top = elevationValue
                shapeDrawablePadding.bottom = elevationValue
                DY = 0
            }
            Gravity.TOP -> {
                shapeDrawablePadding.top = elevationValue * 2
                shapeDrawablePadding.bottom = elevationValue
                DY = -1 * elevationValue / 3
            }
            Gravity.BOTTOM -> {
                shapeDrawablePadding.top = elevationValue
                shapeDrawablePadding.bottom = elevationValue * 2
                DY = elevationValue / 3
            }
            else -> {
                shapeDrawablePadding.top = elevationValue
                shapeDrawablePadding.bottom = elevationValue * 2
                DY = elevationValue / 3
            }
        }

        val shapeDrawable = ShapeDrawable()
        shapeDrawable.setPadding(shapeDrawablePadding)

        shapeDrawable.paint.color = backgroundColorValue
        shapeDrawable.paint.setShadowLayer(shadowRadiusValue, 0f, DY.toFloat(), shadowColorValue)

        view.setLayerType(LAYER_TYPE_SOFTWARE, shapeDrawable.paint)

        shapeDrawable.shape = RoundRectShape(outerRadius, null, null)

        val drawable = LayerDrawable(arrayOf<Drawable>(shapeDrawable))
        drawable.setLayerInset(0, elevationValue, elevationValue, elevationValue, elevationValue)

        return drawable

    }
}