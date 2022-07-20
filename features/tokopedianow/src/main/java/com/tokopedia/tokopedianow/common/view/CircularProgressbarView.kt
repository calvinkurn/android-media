package com.tokopedia.tokopedianow.common.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.annotation.FloatRange
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import android.graphics.BlurMaskFilter
import android.graphics.Paint
import android.graphics.RectF
import com.tokopedia.kotlin.extensions.view.toBitmap

class CircularProgressbarView : View {
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
    private val imageShadowPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    private val circleRect = RectF()
    private val imageRect = RectF()
    private val imageShadowRect = RectF()
    private val startAngle = -80f
    private val maxAngle = 360f
    private val maxProgress = 100

    private var imageBitmap: Bitmap? = null
    private var isImageShadowShowed = false
    private var diameter = 0f
    private var angle = 0f

    override fun onDraw(canvas: Canvas) {
        drawImage(canvas)
        if (!isImageShadowShowed) {
            drawCircle(maxAngle, canvas, backgroundPaint)
            drawCircle(angle, canvas, progressPaint)
        }
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
        if (isImageShadowShowed) {
            setLayerType(LAYER_TYPE_SOFTWARE, imagePaint)
            imageRect.set(25f, 25f, diameter - 25f, diameter - 25f)
            imageShadowRect.set(5f, 5f, diameter - 5f, diameter - 5f)
            imageShadowPaint.color = Color.TRANSPARENT
            imageShadowPaint.strokeWidth = 0f
            imageShadowPaint.setShadowLayer(4f, 0f, 0f, Color.argb(128, 0, 0, 0))
            canvas.drawArc(imageShadowRect, startAngle, maxAngle, false, imageShadowPaint)
        } else {
            val strokeWidth = backgroundPaint.strokeWidth
            imageRect.set(strokeWidth + strokeWidth, strokeWidth + strokeWidth, diameter - strokeWidth - strokeWidth, diameter - strokeWidth - strokeWidth)
        }

        imageBitmap?.apply { canvas.drawBitmap(this, null, imageRect, imagePaint) }
    }

    private fun calculateAngle(progress: Float) = maxAngle / maxProgress * progress

    fun setImageResId(drawable: Drawable?, isShadowShowed: Boolean) {
        imageBitmap = drawable?.toBitmap()
        isImageShadowShowed = isShadowShowed
        invalidate()
    }

    fun setImageUrl(url: String, isShadowShowed: Boolean, defaultDrawable: Drawable?) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .error(defaultDrawable)
            .into(object : CustomTarget<Bitmap>(){
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    imageBitmap = errorDrawable?.toBitmap()
                    isImageShadowShowed = isShadowShowed
                    invalidate()
                }
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    imageBitmap = resource
                    isImageShadowShowed = isShadowShowed
                    invalidate()
                }
                override fun onLoadCleared(placeholder: Drawable?) {  }
            })
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