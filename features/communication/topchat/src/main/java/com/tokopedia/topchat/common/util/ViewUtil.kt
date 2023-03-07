package com.tokopedia.topchat.common.util

import android.content.Context
import android.content.res.Resources.getSystem
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.StateListDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.provider.Settings
import android.util.StateSet
import android.view.Gravity
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowLayoutInfo
import com.tokopedia.kotlin.extensions.view.toPx

object ViewUtil {

    private const val ELEVATION_VALUE_DIVIDER = 3f
    private const val FIVE_MARGIN = 5
    private const val ELLIPSIZE = "..."

    // Fold-able State
    const val EMPTY_STATE = 0
    const val FLAT_STATE = 1
    const val HALF_OPEN_STATE = 2

    @Suppress("MagicNumber")
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
                shadowDrawableRect.top = shadowTop ?: elevationValue
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
            drawable.setLayerInset(
                0,
                elevationValue,
                elevationValue,
                elevationValue,
                elevationValue
            )
        } else {
            drawable.setLayerInset(
                0,
                elevationValue,
                elevationValue,
                elevationValue,
                shadowDrawableRect.bottom
            )
        }

        if (strokeColor != null && strokeWidthValue != null && drawableLayer.size > 1) {
            val strokeMargin = strokeWidthValue.toInt() / 2
            drawable.setLayerInset(1, strokeMargin, strokeMargin, strokeMargin, strokeMargin)
        }

        if (pressedDrawable != null) {
            stateDrawable.addState(
                intArrayOf(android.R.attr.state_pressed),
                pressedDrawable
            )
        }
        stateDrawable.addState(StateSet.WILD_CARD, drawable)

        return stateDrawable
    }

    fun areSystemAnimationsEnabled(context: Context?): Boolean {
        if (context == null) return false
        val duration: Float = Settings.Global.getFloat(
            context.contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE,
            0f
        )
        val transition: Float = Settings.Global.getFloat(
            context.contentResolver,
            Settings.Global.TRANSITION_ANIMATION_SCALE,
            0f
        )
        return duration != 0f && transition != 0f
    }

    fun alignViewToDeviceFeatureBoundaries(
        newLayoutInfo: WindowLayoutInfo,
        constraintLayoutParent: ConstraintLayout?,
        @IdRes firstContainerId: Int,
        @IdRes secondContainerId: Int,
        @IdRes toolbarId: Int,
        @IdRes deviceFeatureId: Int
    ): Boolean {
        var result = false
        val set = ConstraintSet()
        set.clone(constraintLayoutParent)

        val rect = newLayoutInfo.displayFeatures[0].bounds
        setupDeviceFeatureLine(set, toolbarId, deviceFeatureId)

        if (rect.top == 0) {
            // Device feature is placed vertically
            set.setMargin(deviceFeatureId, ConstraintSet.START, rect.left)
            setupVerticalFlex(
                set,
                firstContainerId,
                secondContainerId,
                toolbarId,
                deviceFeatureId
            )
            result = true
        }

        set.setVisibility(deviceFeatureId, View.VISIBLE)
        set.applyTo(constraintLayoutParent)
        return result
    }

    private fun setupDeviceFeatureLine(
        set: ConstraintSet,
        @IdRes toolbarId: Int,
        @IdRes deviceFeatureId: Int
    ) {
        set.connect(
            deviceFeatureId,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START,
            0
        )
        set.connect(
            deviceFeatureId,
            ConstraintSet.TOP,
            toolbarId,
            ConstraintSet.BOTTOM,
            0
        )
    }

    private fun setupVerticalFlex(
        set: ConstraintSet,
        @IdRes firstContainerId: Int,
        @IdRes secondContainerId: Int,
        @IdRes toolbarId: Int,
        @IdRes deviceFeatureId: Int
    ) {
        set.connect(
            firstContainerId,
            ConstraintSet.TOP,
            toolbarId,
            ConstraintSet.BOTTOM,
            0
        )
        set.connect(
            firstContainerId,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM,
            0
        )
        set.connect(
            firstContainerId,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START,
            0
        )
        set.connect(
            firstContainerId,
            ConstraintSet.END,
            deviceFeatureId,
            ConstraintSet.START,
            0
        )

        set.connect(
            secondContainerId,
            ConstraintSet.TOP,
            toolbarId,
            ConstraintSet.BOTTOM,
            FIVE_MARGIN
        )
        set.connect(
            secondContainerId,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM,
            0
        )
        set.connect(
            secondContainerId,
            ConstraintSet.START,
            deviceFeatureId,
            ConstraintSet.END,
            0
        )
        set.connect(
            secondContainerId,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END,
            0
        )
    }

    fun convertToPx(dp: Int): Int {
        return (dp * getSystem().displayMetrics.density).toInt()
    }

    fun ellipsizeLongText(text: String, maxChar: Int): String {
        var resultText = text
        if (text.length > maxChar) {
            resultText = resultText.substring(0, maxChar)
            // Remove if last char is whitespace
            val lastCharIndex = maxChar - 1
            if (lastCharIndex > 0 && resultText[lastCharIndex].toString() == " ") {
                resultText = resultText.substring(0, lastCharIndex)
            }
            resultText += ELLIPSIZE
        }
        return resultText
    }

    fun getFoldingFeatureState(foldingState: FoldingFeature.State?): Int {
        return when (foldingState) {
            FoldingFeature.State.FLAT -> FLAT_STATE
            FoldingFeature.State.HALF_OPENED -> HALF_OPEN_STATE
            else -> EMPTY_STATE
        }
    }
}
