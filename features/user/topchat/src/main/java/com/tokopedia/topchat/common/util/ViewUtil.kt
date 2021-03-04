package com.tokopedia.topchat.common.util

import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.view.Gravity
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.toPx


object ViewUtil {
    fun generateBackgroundWithShadow(
            view: View?,
            @ColorRes backgroundColor: Int,
            @DimenRes topLeftRadius: Int,
            @DimenRes topRightRadius: Int,
            @DimenRes bottomLeftRadius: Int,
            @DimenRes bottomRightRadius: Int,
            @ColorRes shadowColor: Int,
            @DimenRes elevation: Int,
            @DimenRes shadowRadius: Int,
            shadowGravity: Int,
            @ColorRes strokeColor: Int? = null,
            @DimenRes strokeWidth: Int? = null
    ): Drawable? {
        if (view == null) return null
        val topLeftRadiusValue = view.context.resources.getDimension(topLeftRadius)
        val topRightRadiusValue = view.context.resources.getDimension(topRightRadius)
        val bottomLeftRadiusValue = view.context.resources.getDimension(bottomLeftRadius)
        val bottomRightRadiusValue = view.context.resources.getDimension(bottomRightRadius)
        val elevationValue = view.context.resources.getDimension(elevation).toInt()
        val shadowRadiusValue = view.context.resources.getDimension(shadowRadius)
        val shadowColorValue = ContextCompat.getColor(view.context, shadowColor)
        val backgroundColorValue = ContextCompat.getColor(view.context, backgroundColor)
        val strokeColorValue: Int? = strokeColor?.let { ContextCompat.getColor(view.context, strokeColor) }
        val strokeWidthValue: Float? = strokeWidth?.let { view.context.resources.getDimension(strokeWidth) }

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

        val DY: Float
        when (shadowGravity) {
            Gravity.CENTER -> {
                shapeDrawablePadding.top = elevationValue
                shapeDrawablePadding.bottom = elevationValue
                DY = 0.5f.toPx()
            }
            Gravity.TOP -> {
                shapeDrawablePadding.top = elevationValue * 2
                shapeDrawablePadding.bottom = elevationValue
                DY = -1 * elevationValue / 3f
            }
            Gravity.BOTTOM -> {
                shapeDrawablePadding.top = elevationValue
                shapeDrawablePadding.bottom = elevationValue * 2
                DY = elevationValue / 3f
            }
            else -> {
                shapeDrawablePadding.top = elevationValue
                shapeDrawablePadding.bottom = elevationValue * 2
                DY = elevationValue / 3f
            }
        }

        val shapeDrawable = ShapeDrawable().apply {
            setPadding(shapeDrawablePadding)
            paint.color = backgroundColorValue
            paint.setShadowLayer(shadowRadiusValue, 0f, DY, shadowColorValue)
            shape = RoundRectShape(outerRadius, null, null)
        }
        val drawableLayer = arrayListOf<Drawable>(shapeDrawable)
        if (strokeColorValue != null && strokeWidthValue != null) {
            val strokeDrawable = ShapeDrawable().apply {
                setPadding(shapeDrawablePadding)
                paint.style = Paint.Style.STROKE
                paint.color = strokeColorValue
                paint.strokeWidth = strokeWidthValue
                shape = RoundRectShape(outerRadius, null, null)
            }
            drawableLayer.add(strokeDrawable)
        }

        val drawable = LayerDrawable(drawableLayer.toTypedArray())
        drawable.setLayerInset(0, elevationValue, elevationValue, elevationValue, elevationValue)

        if (strokeColor != null && strokeWidthValue != null && drawableLayer.size > 1) {
            val strokeMargin = strokeWidthValue.toInt() / 2
            drawable.setLayerInset(1, strokeMargin, strokeMargin, strokeMargin, strokeMargin)
        }

        return drawable

    }
}