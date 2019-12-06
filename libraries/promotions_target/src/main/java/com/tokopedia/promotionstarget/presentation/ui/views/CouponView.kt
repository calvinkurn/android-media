package com.tokopedia.promotionstarget.presentation.ui.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.tokopedia.promotionstarget.R

class CouponView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardConstraintLayout(context, attrs, defStyleAttr) {

    private var circleRadius = 30f
    private var circleColor = Color.WHITE

    override fun readAttrs(attrs: AttributeSet?) {
        super.readAttrs(attrs)
        if (attrs != null) {
            val typedArray =
                    context.theme.obtainStyledAttributes(attrs, R.styleable.CouponView, 0, 0)
            circleColor = typedArray.getColor(R.styleable.CouponView_cvCircleColor, Color.WHITE)
            circleRadius = typedArray.getDimension(R.styleable.CouponView_cvCircleRadius, dpToPx(context, 24))
            typedArray.recycle()
        }
    }

    override fun drawChild(canvas: Canvas?, child: View?, drawingTime: Long): Boolean {
        val shouldDrawChild = super.drawChild(canvas, child, drawingTime)
        val imageHeight = getImageViewHeight()
        if (imageHeight <= 0) {
            cutTwoCircles(canvas!!)
        } else {
            cutTwoCircles(canvas!!, imageHeight.toFloat() / 2)
        }

        return shouldDrawChild
    }

    private fun getImageViewHeight(): Int {
        val imageView = findViewById<ImageView>(R.id.appCompatImageView)
        if(imageView == null) return 0
        else return imageView.height
    }

    private fun cutTwoCircles(canvas: Canvas, heightFromTop: Float = -1f) {
        canvas.save()
        clipPath.reset()

        clipRectF.top = 0f
        clipRectF.left = 0f
        val x = 0f
        val y: Float
        if (heightFromTop == -1f) {
            y = (canvas.height / 3f)
        } else {
            y = heightFromTop
        }
        val circleRadius = circleRadius
        val p = Paint()
        p.style = Paint.Style.FILL
        p.color = circleColor
        p.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
        p.isAntiAlias = true

        clipPath.addCircle(x, y, circleRadius, Path.Direction.CW)
        clipPath.addCircle(canvas.width.toFloat(), y, circleRadius, Path.Direction.CW)
        canvas.drawPath(clipPath, p)
        canvas.clipPath(clipPath)
        canvas.restore()
    }
}