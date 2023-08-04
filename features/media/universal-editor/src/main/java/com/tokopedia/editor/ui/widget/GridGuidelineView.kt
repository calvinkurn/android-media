package com.tokopedia.editor.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker

class GridGuidelineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint: Paint = Paint()
    private var showHorizontalLine = false
    private var showVerticalLine = false

    init {
        val color = MethodChecker.getColor(
            context,
            com.tokopedia.unifyprinciples.R.color.Unify_GN500
        )

        paint.color = color
        paint.strokeWidth = 2f
    }

    fun setShowHorizontalLine(show: Boolean) {
        showHorizontalLine = show
        invalidate()
    }

    fun setShowVerticalLine(show: Boolean) {
        showVerticalLine = show
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (showHorizontalLine) {
            val centerY = height / 2f
            canvas.drawLine(0f, centerY, width.toFloat(), centerY, paint)
        }

        if (showVerticalLine) {
            val centerX = width / 2f
            canvas.drawLine(centerX, 0f, centerX, height.toFloat(), paint)
        }
    }
}
