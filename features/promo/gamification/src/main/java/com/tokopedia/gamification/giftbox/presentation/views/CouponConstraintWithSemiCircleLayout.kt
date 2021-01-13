package com.tokopedia.gamification.giftbox.presentation.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.gamification.giftbox.presentation.helpers.dpToPx

class CouponConstraintWithSemiCircleLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CouponConstraintLayout(context, attrs, defStyleAttr) {

    private val circlePaint: Paint = Paint()
    private var circleClipRectF = RectF()
    private val circleClipPath = Path()
    private var circleRadius = 0f

    init {
        circlePaint.style = Paint.Style.FILL
        circlePaint.color = Color.RED
        circlePaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        circlePaint.isAntiAlias = true
        circleRadius = dpToPx(12)
    }

    fun drawCircles(canvas: Canvas){
        circleClipPath.reset()
        circleClipRectF.top = 0f
        circleClipRectF.left = 0f
        val x = 0f
        val y = height / 2f
        val circleRadius = circleRadius

        circleClipPath.addCircle(x, y, circleRadius, Path.Direction.CW)
        circleClipPath.addCircle(width.toFloat(), y, circleRadius, Path.Direction.CW)
        canvas.drawPath(circleClipPath, circlePaint)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        drawCircles(canvas)
    }
}