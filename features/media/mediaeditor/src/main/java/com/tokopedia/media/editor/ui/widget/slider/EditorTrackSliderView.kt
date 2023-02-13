package com.tokopedia.media.editor.ui.widget.slider

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.media.editor.R
import com.tokopedia.unifyprinciples.R as PrincipleR

class EditorTrackSliderView(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {

    private val paint = Paint()
    private var xStart = 0f
    private var xEnd = 0f
    private var yCenter = 0f

    init {
        val strokeWidth = resources.getDimension(R.dimen.media_editor_slider_height)
        paint.strokeWidth = strokeWidth
        paint.color = ContextCompat.getColor(context, PrincipleR.color.Unify_GN400)
        yCenter = strokeWidth / 2
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
}