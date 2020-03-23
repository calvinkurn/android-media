package com.tokopedia.gamification.giftbox.presentation.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.gamification.giftbox.presentation.helpers.dpToPx

class CouponImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val paint: Paint
    private var clipRectF = RectF()
    private val clipPath = Path()
    private var circleRadius = 0f

    init {
        setLayerType(ConstraintLayout.LAYER_TYPE_HARDWARE, null)

        paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = Color.TRANSPARENT
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        paint.isAntiAlias = true
        circleRadius = dpToPx(12)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {

            clipPath.reset()
            clipRectF.top = 0f
            clipRectF.left = 0f
            val x = 0f
            val y = height/2f
            val circleRadius = circleRadius

            clipPath.addCircle(x, y, circleRadius, Path.Direction.CW)
            clipPath.addCircle(width.toFloat(), y, circleRadius, Path.Direction.CW)
            canvas.drawPath(clipPath, paint)
        }
    }
}