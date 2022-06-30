package com.tokopedia.media.editor.ui.component.slider

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt

class MediaEditorSliderTrack(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {

    private val paint = Paint()
    private var xStart = 0f
    private var xEnd = 0f
    private var yCenter = 0f

    init {
        paint.strokeWidth = DEFAULT_STROKE_WIDTH
        paint.color = DEFAULT_COLOR

        post {
            yCenter = (height / 2).toFloat()
        }
    }

    fun setLine(height: Float, @ColorInt color: Int) {
        paint.strokeWidth = height
        paint.color = color
    }

    fun update(newXStart: Float, newXEnd: Float, thumbSize: Float) {
        xStart = newXStart
        xEnd = newXEnd + thumbSize

        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            canvas.drawLine(xStart, yCenter, xEnd, yCenter, paint)
        }
    }

    companion object {
        private const val DEFAULT_STROKE_WIDTH = 5f
        private const val DEFAULT_COLOR = Color.RED
    }
}