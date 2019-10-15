package com.tokopedia.promotionstarget.views

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.tokopedia.promotionstarget.R
import com.tokopedia.targetpromotions.CardConstraintLayout

class CouponView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardConstraintLayout(context, attrs, defStyleAttr) {

    override fun drawChild(canvas: Canvas?, child: View?, drawingTime: Long): Boolean {
        val a = super.drawChild(canvas, child, drawingTime)
        cutTwoCircles(canvas!!)
        return a

    }

    private fun cutTwoCircles(canvas: Canvas) {
        canvas.save()
        clipPath.reset()

        clipRectF.top = 0f
        clipRectF.left = 0f
        val x = 0f
        val y = canvas.height.toFloat() / 3f
        val circleRadius = 30f
        val p = Paint()
        p.style = Paint.Style.FILL
        p.color = ContextCompat.getColor(context, R.color.t_promo_halfCircleColor)
        p.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
        p.isAntiAlias = true

        clipPath.addCircle(x, y, circleRadius, Path.Direction.CW)
        clipPath.addCircle(canvas.width.toFloat(), y, circleRadius, Path.Direction.CW)
        canvas.drawPath(clipPath,p)
        canvas.clipPath(clipPath)
        canvas.restore()
    }
}