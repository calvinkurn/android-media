package com.tokopedia.shop.common.util

/**
 * Created by Yehezkiel on 06/10/20
 */

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

/**
 * Created by Yehezkiel on 06/10/20
 */
object RoundedShadowUtill {

    private const val DY_CENTER = 1f
    private const val DY_TOP = -1
    private const val DY_ELEVATION_DIVIDER = 3f
    private const val SHADOW_LAYER_DIVIDER = 3
    private const val SHADOW_LAYER_DX = 0f

    fun generateBackgroundWithShadow(view: View,
                                     @ColorRes backgroundColor: Int,
                                     @DimenRes cornerRadius: Int,
                                     @ColorRes shadowColor: Int,
                                     @DimenRes elevation: Int,
                                     shadowGravity: Int): Drawable {
        val cornerRadiusValue = view.context.resources.getDimension(cornerRadius)
        val elevationValue = view.context.resources.getDimension(elevation).toInt()
        val shadowColorValue = ContextCompat.getColor(view.context, shadowColor)
        val backgroundColorValue = ContextCompat.getColor(view.context, backgroundColor)

        val outerRadius = floatArrayOf(
                cornerRadiusValue, cornerRadiusValue, cornerRadiusValue, cornerRadiusValue,
                0F, 0F, 0F, 0F
        )

        val backgroundPaint = Paint()
        backgroundPaint.style = Paint.Style.FILL
        backgroundPaint.setShadowLayer(cornerRadiusValue, 0f, 0f, 0)

        val shapeDrawablePadding = Rect()
        shapeDrawablePadding.left = elevationValue
        shapeDrawablePadding.right = elevationValue

        val DY: Float
        when (shadowGravity) {
            Gravity.CENTER -> {
                shapeDrawablePadding.top = elevationValue
                shapeDrawablePadding.bottom = elevationValue
                DY = DY_CENTER
            }
            Gravity.TOP -> {
                shapeDrawablePadding.top = elevationValue * 2
                shapeDrawablePadding.bottom = elevationValue
                DY = DY_TOP * elevationValue / DY_ELEVATION_DIVIDER
            }
            Gravity.BOTTOM -> {
                shapeDrawablePadding.top = elevationValue
                shapeDrawablePadding.bottom = elevationValue * 2
                DY = elevationValue / DY_ELEVATION_DIVIDER
            }
            else -> {
                shapeDrawablePadding.top = elevationValue
                shapeDrawablePadding.bottom = elevationValue * 2
                DY = elevationValue / DY_ELEVATION_DIVIDER
            }
        }

        val shapeDrawable = ShapeDrawable()
        shapeDrawable.setPadding(shapeDrawablePadding)
        shapeDrawable.paint.color = backgroundColorValue
        shapeDrawable.paint.setShadowLayer(cornerRadiusValue / SHADOW_LAYER_DIVIDER, SHADOW_LAYER_DX, DY, shadowColorValue)

        view.setLayerType(LAYER_TYPE_SOFTWARE, shapeDrawable.paint)

        shapeDrawable.shape = RoundRectShape(outerRadius, null, null)

        val drawable = LayerDrawable(arrayOf<Drawable>(shapeDrawable))
        drawable.setLayerInset(0, 0, elevationValue * 2, 0, 0)

        return drawable
    }
}