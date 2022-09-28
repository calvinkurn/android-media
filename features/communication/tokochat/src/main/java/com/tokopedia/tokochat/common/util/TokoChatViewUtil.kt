package com.tokopedia.tokochat.common.util

import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.StateListDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.StateSet
import android.view.Gravity
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.toPx

object TokoChatViewUtil {

    private const val ELEVATION_VALUE_DIVIDER = 3f

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
        @DimenRes strokeWidth: Int? = null,
        strokePaddingBottom: Int? = null,
        useViewPadding: Boolean = false,
        pressedDrawable: Drawable? = null,
        shadowTop: Int? = null,
        isInsetElevation: Boolean = true
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
        val strokeColorValue: Int? =
            strokeColor?.let { ContextCompat.getColor(view.context, strokeColor) }
        val strokeWidthValue: Float? =
            strokeWidth?.let { view.context.resources.getDimension(strokeWidth) }

        val stateDrawable = StateListDrawable()
        val shadowDrawable = ShapeDrawable()
        val strokeDrawable = ShapeDrawable()
        val drawableLayer = arrayListOf<Drawable>()

        val outerRadius = floatArrayOf(
            topLeftRadiusValue,
            topLeftRadiusValue,
            topRightRadiusValue,
            topRightRadiusValue,
            bottomLeftRadiusValue,
            bottomLeftRadiusValue,
            bottomRightRadiusValue,
            bottomRightRadiusValue
        )

        val backgroundPaint = Paint()
        backgroundPaint.style = Paint.Style.FILL
        backgroundPaint.setShadowLayer(shadowRadiusValue, 0f, 0f, 0)

        val shadowDrawableRect = Rect()
        shadowDrawableRect.left = elevationValue
        shadowDrawableRect.right = elevationValue

        val DY: Float
        when (shadowGravity) {
            Gravity.CENTER -> {
                shadowDrawableRect.top = elevationValue
                shadowDrawableRect.bottom = elevationValue
                DY = 0.5f.toPx()
            }
            Gravity.TOP -> {
                shadowDrawableRect.top = elevationValue * 2
                shadowDrawableRect.bottom = elevationValue
                DY = -1 * elevationValue / ELEVATION_VALUE_DIVIDER
            }
            Gravity.BOTTOM -> {
                shadowDrawableRect.top = elevationValue
                shadowDrawableRect.bottom = elevationValue * 2
                DY = elevationValue / ELEVATION_VALUE_DIVIDER
            }
            else -> {
                shadowDrawableRect.top = shadowTop?: elevationValue
                shadowDrawableRect.bottom = elevationValue * 2
                DY = elevationValue / ELEVATION_VALUE_DIVIDER
            }
        }

        if (useViewPadding) {
            if (view.paddingTop > shadowDrawableRect.top) {
                shadowDrawableRect.top += view.paddingTop
            }
            if (view.paddingBottom > shadowDrawableRect.bottom) {
                shadowDrawableRect.bottom += view.paddingBottom
            }
            if (view.paddingStart > shadowDrawableRect.left) {
                shadowDrawableRect.left += view.paddingStart
            }
            if (view.paddingEnd > shadowDrawableRect.right) {
                shadowDrawableRect.right += view.paddingEnd
            }
        }

        shadowDrawable.apply {
            setPadding(shadowDrawableRect)
            paint.color = backgroundColorValue
            paint.setShadowLayer(shadowRadiusValue, 0f, DY, shadowColorValue)
            shape = RoundRectShape(outerRadius, null, null)
        }
        drawableLayer.add(shadowDrawable)

        if (strokePaddingBottom != null) {
            shadowDrawableRect.bottom = strokePaddingBottom
        }

        if (strokeColorValue != null && strokeWidthValue != null) {
            strokeDrawable.apply {
                setPadding(shadowDrawableRect)
                paint.style = Paint.Style.STROKE
                paint.color = strokeColorValue
                paint.strokeWidth = strokeWidthValue
                shape = RoundRectShape(outerRadius, null, null)
            }
            drawableLayer.add(strokeDrawable)
        }

        val drawable = LayerDrawable(drawableLayer.toTypedArray())
        if (isInsetElevation) {
            drawable.setLayerInset(0,
                elevationValue, elevationValue, elevationValue, elevationValue)
        } else {
            drawable.setLayerInset(0,
                elevationValue, elevationValue, elevationValue, shadowDrawableRect.bottom)
        }

        if (strokeColor != null && strokeWidthValue != null && drawableLayer.size > 1) {
            val strokeMargin = strokeWidthValue.toInt() / 2
            drawable.setLayerInset(1, strokeMargin, strokeMargin, strokeMargin, strokeMargin)
        }

        if (pressedDrawable != null) {
            stateDrawable.addState(
                intArrayOf(android.R.attr.state_pressed), pressedDrawable
            )
        }
        stateDrawable.addState(StateSet.WILD_CARD, drawable)

        return stateDrawable
    }
}
