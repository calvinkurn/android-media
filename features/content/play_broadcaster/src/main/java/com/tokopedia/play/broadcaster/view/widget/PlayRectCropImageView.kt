package com.tokopedia.play.broadcaster.view.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.play.broadcaster.R

/**
 * @author by furqan on 03/06/2020
 */
class PlayRectCropImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : AppCompatImageView(context, attrs, defStyleAttr) {

    val centerRect: RectF

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        val centerOfCanvas = Point(width / 2, height / 2)
        val rectW = resources.getDimensionPixelSize(R.dimen.play_cover_width)
        val rectH = resources.getDimensionPixelSize(R.dimen.play_cover_height)
        val left = (centerOfCanvas.x - (rectW / 2)).toFloat()
        val top = 0f
        val right = (centerOfCanvas.x + (rectW / 2)).toFloat()
        val bottom = height.toFloat()
        centerRect = RectF(left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw Overlay
        val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.parseColor("#33C4C4C4")
        paint.style = Paint.Style.FILL
        canvas.drawPaint(paint)

        //Draw transparent shape
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        canvas.drawRoundRect(centerRect, CENTER_RECT_RADIUS, CENTER_RECT_RADIUS, paint)
    }

    companion object {
        private const val CENTER_RECT_RADIUS = 8f
    }

}