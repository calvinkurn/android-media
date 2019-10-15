package com.tokopedia.promotionstarget.ui.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.tokopedia.promotionstarget.R
import com.tokopedia.targetpromotions.CardConstraintLayout

class CouponView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardConstraintLayout(context, attrs, defStyleAttr) {

    private var circleRadius = 30f
    private var circleColor = Color.WHITE

    override fun readAttrs(attrs: AttributeSet?) {
        super.readAttrs(attrs)
        if (attrs != null) {
            val typedArray =
                    context.theme.obtainStyledAttributes(attrs, R.styleable.CountDownView, 0, 0)
            circleColor = typedArray.getColor(R.styleable.CouponView_cvCircleColor, Color.WHITE)
            circleRadius = typedArray.getDimension(R.styleable.CouponView_cvCircleRadius, dpToPx(context, 8))
            typedArray.recycle()
        }
    }

    override fun drawChild(canvas: Canvas?, child: View?, drawingTime: Long): Boolean {
        val shouldDrawChild = super.drawChild(canvas, child, drawingTime)
        cutTwoCircles(canvas!!)
        return shouldDrawChild
    }

    private fun cutTwoCircles(canvas: Canvas) {
        canvas.save()
        clipPath.reset()

        clipRectF.top = 0f
        clipRectF.left = 0f
        val x = 0f
        val y = canvas.height.toFloat() / 3f
        val circleRadius = circleRadius
        val p = Paint()
        p.style = Paint.Style.FILL
        p.color = circleColor
        p.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
        p.isAntiAlias = true

        clipPath.addCircle(x, y, circleRadius, Path.Direction.CW)
        clipPath.addCircle(canvas.width.toFloat(), y, circleRadius, Path.Direction.CW)
        canvas.drawPath(clipPath, p)
        canvas.clipPath(clipPath)
        canvas.restore()
    }
}