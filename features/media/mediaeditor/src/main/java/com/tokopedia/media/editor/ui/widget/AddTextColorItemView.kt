package com.tokopedia.media.editor.ui.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.R as principleR

class AddTextColorItemView(context: Context) : AppCompatImageView(context) {
    private val shapeDrawable = GradientDrawable()

    private val strokeInactiveColor = principleR.color.Unify_NN0
    private val strokeActiveColor = principleR.color.Unify_GN500

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
            ContextCompat.getColor(context, strokeInactiveColor)
        )
    }

    fun setActive() {
        shapeDrawable.setStroke(
            COLOR_CIRCLE_STROKE.toPx(),
            ContextCompat.getColor(context, strokeActiveColor)
        )
    }

    fun setInactive() {
        shapeDrawable.setStroke(
            COLOR_CIRCLE_STROKE.toPx(),
            ContextCompat.getColor(context, strokeInactiveColor)
        )
    }

    companion object {
        private const val COLOR_CIRCLE_SIZE = 32
        private const val COLOR_CIRCLE_STROKE = 2
        private const val COLOR_CIRCLE_MARGIN = 16
    }
}
