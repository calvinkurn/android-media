package com.tokopedia.editor.ui.components

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.R as principleR

class InputTextColorItemView(context: Context) : AppCompatImageView(context) {
    private val shapeDrawable = GradientDrawable()

    private val strokeColor = ContextCompat.getColor(context, principleR.color.Unify_NN0)

    init {
        val lp = LinearLayout.LayoutParams(
            COLOR_CIRCLE_SIZE.toPx(),
            COLOR_CIRCLE_SIZE.toPx()
        )
        lp.rightMargin = COLOR_CIRCLE_MARGIN.toPx()
        layoutParams = lp

        shapeDrawable.shape = GradientDrawable.OVAL
        shapeDrawable.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)

        setImageDrawable(shapeDrawable)
    }

    fun setColor(colorRef: Int) {
        shapeDrawable.setColor(colorRef)
        shapeDrawable.setStroke(
            COLOR_CIRCLE_STROKE.toPx(),
            strokeColor
        )
    }

    fun setActive() {
        shapeDrawable.setStroke(
            COLOR_CIRCLE_STROKE_ACTIVE.toPx(),
            strokeColor
        )
    }

    fun setInactive() {
        shapeDrawable.setStroke(
            COLOR_CIRCLE_STROKE.toPx(),
            strokeColor
        )
    }

    companion object {
        private const val COLOR_CIRCLE_SIZE = 32
        private const val COLOR_CIRCLE_STROKE = 2
        private const val COLOR_CIRCLE_STROKE_ACTIVE = 4
        private const val COLOR_CIRCLE_MARGIN = 16
    }
}
