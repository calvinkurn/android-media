package com.tokopedia.promocheckout.common.view.widget

import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.view.View
import android.view.View.LAYER_TYPE_SOFTWARE
import androidx.core.content.ContextCompat
import com.tokopedia.promocheckout.common.R


/**
 * Created by Lukas on 2019-08-20
 */
object ViewUtils {
    fun generateBackgroundWithShadow(view: View): Drawable {
        val cornerRadiusValue = view.context.resources.getDimension(R.dimen.layout_lvl1)
        val elevationValue = view.context.resources.getDimension(R.dimen.spacing_lvl1).toInt()
        val shadowColorValue = ContextCompat.getColor(view.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44)
        val backgroundColorValue = ContextCompat.getColor(view.context, com.tokopedia.unifyprinciples.R.color.Unify_N0)

        val outerRadius = floatArrayOf(cornerRadiusValue, cornerRadiusValue, cornerRadiusValue, cornerRadiusValue, cornerRadiusValue, cornerRadiusValue, cornerRadiusValue, cornerRadiusValue)

        val backgroundPaint = Paint()
        backgroundPaint.style = Paint.Style.FILL
        backgroundPaint.setShadowLayer(cornerRadiusValue, 0f, 0f, 0)

        val shapeDrawablePadding = Rect()
        shapeDrawablePadding.left = elevationValue
        shapeDrawablePadding.right = elevationValue

        shapeDrawablePadding.top = elevationValue
        shapeDrawablePadding.bottom = elevationValue

        val shapeDrawable = ShapeDrawable()
        shapeDrawable.setPadding(shapeDrawablePadding)

        shapeDrawable.paint.color = backgroundColorValue
        shapeDrawable.paint.setShadowLayer(cornerRadiusValue / 3, 0f, 0.toFloat(), shadowColorValue)

        view.setLayerType(LAYER_TYPE_SOFTWARE, shapeDrawable.paint)

        shapeDrawable.shape = RoundRectShape(outerRadius, null, null)

        val drawable = LayerDrawable(arrayOf<Drawable>(shapeDrawable))
        drawable.setLayerInset(0, elevationValue, elevationValue * 2, elevationValue, elevationValue * 2)

        return drawable

    }

    fun generateBackgroundWithoutShadow(view: View): Drawable {
        val cornerRadiusValue = view.context.resources.getDimension(R.dimen.layout_lvl1)
        val elevationValue = view.context.resources.getDimension(R.dimen.spacing_lvl1).toInt()
        val backgroundColorValue = ContextCompat.getColor(view.context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        val borderColorValue = ContextCompat.getColor(view.context, com.tokopedia.unifyprinciples.R.color.Unify_N100)

        val outerRadius = floatArrayOf(cornerRadiusValue, cornerRadiusValue, cornerRadiusValue, cornerRadiusValue, cornerRadiusValue, cornerRadiusValue, cornerRadiusValue, cornerRadiusValue)

        val backgroundPaint = Paint()
        backgroundPaint.style = Paint.Style.FILL
        backgroundPaint.setShadowLayer(cornerRadiusValue, 0f, 0f, 0)

        val border = Paint()
        border.style = Paint.Style.STROKE
        border.color = borderColorValue
        border.strokeWidth = 4f

        val shapeDrawablePadding = Rect()
        shapeDrawablePadding.left = elevationValue
        shapeDrawablePadding.right = elevationValue

        shapeDrawablePadding.top = elevationValue
        shapeDrawablePadding.bottom = elevationValue

        val shapeDrawable = ShapeDrawable()
        shapeDrawable.setPadding(shapeDrawablePadding)

        shapeDrawable.paint.color = backgroundColorValue
        shapeDrawable.paint.apply {
            set(border)
        }

        view.setLayerType(LAYER_TYPE_SOFTWARE, shapeDrawable.paint)

        shapeDrawable.shape = RoundRectShape(outerRadius, null, null)

        val drawable = LayerDrawable(arrayOf<Drawable>(shapeDrawable))
        drawable.setLayerInset(0, elevationValue, elevationValue * 2, elevationValue, elevationValue * 2)

        return drawable

    }
}