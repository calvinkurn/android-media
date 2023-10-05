package com.tokopedia.tokochat.common.util

import android.content.Context
import android.content.res.Resources
import android.content.res.Resources.getSystem
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
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
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getBitmap
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.tokochat.common.util.TokoChatValueUtil.CENSOR_TEXT
import com.tokopedia.tokochat.common.util.TokoChatValueUtil.MAX_PERCENTAGE
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.toPx
import kotlin.math.roundToInt

object TokoChatViewUtil {

    private const val ELEVATION_VALUE_DIVIDER = 3f
    private const val ICON_BOUND_SIZE = 24
    private const val ELLIPSIZE = "..."

    const val ZERO_DP = 1
    const val ONE_DP = 1
    const val TWO_DP = 2
    const val THREE_DP = 3
    const val FOUR_DP = 4
    const val EIGHT_DP = 8
    const val TEN_DP = 10
    const val ELEVEN_DP = 11
    const val TWENTY_DP = 20
    const val THIRTY_EIGHT_DP = 38
    const val FORTY_EIGHT_DP = 48

    @Suppress("MagicNumber")
    fun generateBackgroundWithShadow(
        view: View?,
        @ColorRes backgroundColor: Int,
        topLeftRadiusValue: Int,
        topRightRadiusValue: Int,
        bottomLeftRadiusValue: Int,
        bottomRightRadiusValue: Int,
        @ColorRes shadowColor: Int,
        elevationValue: Int,
        shadowRadiusValue: Int,
        shadowGravity: Int,
        @ColorRes strokeColor: Int? = null,
        strokeWidthValue: Int? = null,
        strokePaddingBottom: Int? = null,
        useViewPadding: Boolean = false,
        pressedDrawable: Drawable? = null,
        shadowTop: Int? = null,
        isInsetElevation: Boolean = true
    ): Drawable? {
        if (view == null) return null
        val shadowColorValue = ContextCompat.getColor(view.context, shadowColor)
        val backgroundColorValue = ContextCompat.getColor(view.context, backgroundColor)
        val strokeColorValue: Int? =
            strokeColor?.let { ContextCompat.getColor(view.context, strokeColor) }

        val stateDrawable = StateListDrawable()
        val shadowDrawable = ShapeDrawable()
        val strokeDrawable = ShapeDrawable()
        val drawableLayer = arrayListOf<Drawable>()

        val outerRadius = floatArrayOf(
            topLeftRadiusValue.toPx().toFloat(),
            topLeftRadiusValue.toPx().toFloat(),
            topRightRadiusValue.toPx().toFloat(),
            topRightRadiusValue.toPx().toFloat(),
            bottomLeftRadiusValue.toPx().toFloat(),
            bottomLeftRadiusValue.toPx().toFloat(),
            bottomRightRadiusValue.toPx().toFloat(),
            bottomRightRadiusValue.toPx().toFloat()
        )

        val backgroundPaint = Paint()
        backgroundPaint.style = Paint.Style.FILL
        backgroundPaint.setShadowLayer(
            shadowRadiusValue.toPx().toFloat(),
            0f,
            0f,
            0
        )

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
            paint.setShadowLayer(
                shadowRadiusValue.toPx().toFloat(),
                0f,
                DY,
                shadowColorValue
            )
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
                paint.strokeWidth = strokeWidthValue.toPx().toFloat()
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

    fun getOppositeMargin(context: Context?): Float {
        return context?.resources?.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)
            ?: 0f
    }

    private fun scaledDrawable(bmp: Bitmap?, resources: Resources, width: Int, height: Int): Drawable? {
        return try {
            bmp?.let {
                val bmpScaled = Bitmap.createScaledBitmap(it, width, height, false)
                BitmapDrawable(resources, bmpScaled)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun Toolbar?.setBackIconUnify() {
        this?.context?.let {
            val backIconUnify = getIconUnifyDrawable(it, IconUnify.ARROW_BACK)?.getBitmap()
            val scaleDrawable: Drawable? =
                scaledDrawable(
                    backIconUnify,
                    resources,
                    ICON_BOUND_SIZE.toPx(),
                    ICON_BOUND_SIZE.toPx()
                )
            scaleDrawable?.let { newDrawable ->
                navigationIcon = newDrawable
            }
        }
    }

    fun loadGif(imageUnify: ImageUnify, gifUrl: String) {
        Glide.with(imageUnify.context)
            .asGif()
            .load(gifUrl)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(imageUnify)
    }

    fun censorPlatNumber(platNumber: String, percentageCensor: Int): String {
        try {
            // If percentage higher than 100, return original value
            if (percentageCensor > MAX_PERCENTAGE) return platNumber
            val censorCount = platNumber.length * (percentageCensor / MAX_PERCENTAGE.toDouble())
            val censorCountRounded = censorCount.roundToInt()
            val censoredStar = CENSOR_TEXT.repeat(censorCountRounded)
            return platNumber.replaceRange(Int.ZERO, censorCountRounded, censoredStar)
        } catch (throwable: Throwable) {
            // If error, return the original value
            return platNumber
        }
    }
}
