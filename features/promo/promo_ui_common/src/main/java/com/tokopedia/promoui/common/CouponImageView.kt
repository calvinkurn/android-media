package com.tokopedia.promoui.common

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout

class CouponImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    private val paint: Paint
    private var clipRectF = RectF()
    private val clipPath = Path()
    private var circleRadius = 0f
    private var cornerRadius = 0f

    init {
        setLayerType(ConstraintLayout.LAYER_TYPE_SOFTWARE, null)

        paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = Color.TRANSPARENT
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        paint.isAntiAlias = true
        circleRadius = dpToPx(12)
        cornerRadius = dpToPx(8)
    }

    override fun onDraw(canvas: Canvas) {
        clipCornerRadius(canvas)
        super.onDraw(canvas)
        drawTwoCircles(canvas)
    }

    fun drawTwoCircles(canvas: Canvas) {
        clipPath.reset()
        clipRectF.top = 0f
        clipRectF.left = 0f
        val x = 0f
        val y = height / 2f
        val circleRadius = circleRadius

        clipPath.addCircle(x, y, circleRadius, Path.Direction.CW)
        clipPath.addCircle(width.toFloat(), y, circleRadius, Path.Direction.CW)
        canvas.drawPath(clipPath, paint)
    }

    fun clipCornerRadius(canvas: Canvas) {
        clipPath.reset()
        clipRectF.top = 0f
        clipRectF.left = 0f
        clipRectF.right = canvas.width.toFloat()
        clipRectF.bottom = canvas.height.toFloat()
        val radii = floatArrayOf(cornerRadius, cornerRadius, cornerRadius, cornerRadius, 0f, 0f, 0f, 0f)
        clipPath.addRoundRect(clipRectF, radii, Path.Direction.CW)

        canvas.clipPath(clipPath)
    }
}