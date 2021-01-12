package com.tokopedia.gamification.giftbox.presentation.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class OvoGlowingImage @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    var w: Int = 0
    var h: Int = 0

    var shadowPaint = Paint()
    var shadowPath = Path()
    var porterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
    open var shadowColor = Color.BLACK
    var shadowStrokeWidth = 15f
    open var blurRadius = 50f
    var blurMaskFilter: BlurMaskFilter

    init {
        blurMaskFilter = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL)
        shadowPaint.isAntiAlias = true
        shadowPaint.style = Paint.Style.STROKE
        shadowPaint.color = shadowColor
        shadowPaint.strokeWidth = shadowStrokeWidth
        shadowPaint.xfermode = porterDuffXfermode

        shadowPaint.maskFilter = blurMaskFilter

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let { drawGlowingCircle(it) }

    }

    private fun drawGlowingCircle(canvas: Canvas) {
        canvas.save()
        shadowPath.reset()
//        shadowPath.moveTo((width + xOffset).toFloat(), shadowStartY)
//        shadowPath.lineTo((-xOffset).toFloat(), shadowStartY)
//        shadowPath.lineTo((-xOffset).toFloat(), (height + yOffset).toFloat())
//        shadowPath.lineTo((width + xOffset).toFloat(), (height + yOffset).toFloat())
//        shadowPath.lineTo((width + xOffset).toFloat(), shadowStartY)
        shadowPath.addCircle(0f, 0f, w.toFloat(), Path.Direction.CW)
        canvas.drawPath(shadowPath, shadowPaint)
        canvas.restore()

    }
}