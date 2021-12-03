package com.tokopedia.tokopedianow.common.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.FloatRange
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.toBitmap

class CircularProgressView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val progressPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }
    private val backgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }
    private val imagePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val circleRect = RectF()
    private val imageRect = RectF()
    private val startAngle = -80f
    private val maxAngle = 360f
    private val maxProgress = 100

    private var imageBitmap: Bitmap? = null
    private var isImageShadowShowed = false
    private var diameter = 0f
    private var angle = 0f

    override fun onDraw(canvas: Canvas) {
        drawImage(canvas)
        drawCircle(maxAngle, canvas, backgroundPaint)
        drawCircle(angle, canvas, progressPaint)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        diameter = width.coerceAtMost(height).toFloat()
    }

    private fun drawCircle(angle: Float, canvas: Canvas, paint: Paint) {
        val strokeWidth = backgroundPaint.strokeWidth
        circleRect.set(strokeWidth, strokeWidth, diameter - strokeWidth, diameter - strokeWidth)
        canvas.drawArc(circleRect, startAngle, angle, false, paint)
    }

    private fun drawImage(canvas: Canvas) {
        val strokeWidth = backgroundPaint.strokeWidth
        imageRect.set(strokeWidth, strokeWidth, diameter - strokeWidth + 3, diameter - strokeWidth + 3)

        // show shadow of image
        if (isImageShadowShowed) {
            imagePaint.color = Color.GRAY
            imagePaint.maskFilter = BlurMaskFilter(
                2f /* shadowRadius */,
                BlurMaskFilter.Blur.OUTER
            )
            canvas.drawArc(imageRect, startAngle, maxAngle, false, imagePaint)
        }

        // show image
        imagePaint.maskFilter = null
        imageBitmap?.apply { canvas.drawBitmap(this, null, imageRect, imagePaint) }
    }

    private fun calculateAngle(progress: Float) = maxAngle / maxProgress * progress

    fun setImageShadowShowed(isShowed: Boolean) {
        isImageShadowShowed = isShowed
        invalidate()
    }

    fun setImageResId(resId: Int) {
        imageBitmap = ContextCompat.getDrawable(context, resId)?.toBitmap()
        invalidate()
    }

    fun setProgress(@FloatRange(from = 0.0, to = 100.0) progress: Float) {
        angle = calculateAngle(progress)
        invalidate()
    }

    fun setProgressColor(color: Int) {
        progressPaint.color = color
        invalidate()
    }

    fun setProgressBackgroundColor(color: Int) {
        backgroundPaint.color = color
        invalidate()
    }

    fun setProgressWidth(width: Float) {
        progressPaint.strokeWidth = width
        backgroundPaint.strokeWidth = width
        invalidate()
    }

    fun setRounded(isRounded: Boolean) {
        progressPaint.strokeCap = if (isRounded) Paint.Cap.ROUND else Paint.Cap.BUTT
        invalidate()
    }
}