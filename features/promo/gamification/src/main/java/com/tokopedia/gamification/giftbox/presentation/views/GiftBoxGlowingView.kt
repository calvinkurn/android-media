package com.tokopedia.gamification.giftbox.presentation.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.gamification.R
import com.tokopedia.unifycomponents.toPx

open class GiftBoxGlowingView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var shadowPaint = Paint()
    var shadowPath = Path()
    protected var clipPath = Path()
    protected var clipRectF = RectF()

    var porterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)

    var shadowColor = Color.BLACK
    var shadowStrokeWidth = 15.toPx().toFloat()
    var blurRadius = 10.toPx().toFloat()
    private var cornerRadius = 16.toPx().toFloat()

    val padding = 16.toPx() //taken from xml

    var xOffsetTopRight = -9.toPx().toFloat() - padding
    var xOffsetTopLeft = - xOffsetTopRight
    var xOffsetBottomRight = -7.toPx().toFloat() - padding
    var xOffsetBottomLeft = - xOffsetBottomRight

    var yOffsetTopRight = (8).toPx().toFloat() + padding
    var yOffsetTopLeft = yOffsetTopRight
    var yOffsetBottomLeft = -5.toPx().toFloat()
    var yOffsetBottomRight = yOffsetBottomLeft


    var blurMaskFilter: BlurMaskFilter

    init {
        blurMaskFilter = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL)
        shadowColor = ContextCompat.getColor(context, R.color.gf_glowing_green)
        val bottomPadding = context.resources.getDimension(R.dimen.gami_direct_view_bottom_margin)
        yOffsetBottomLeft = - bottomPadding
        yOffsetBottomRight = yOffsetBottomLeft
    }

    override fun dispatchDraw(canvas: Canvas) {
        clipRadius(canvas)
//        drawShadow(canvas)
        super.dispatchDraw(canvas)
    }

    fun clipRadius(canvas: Canvas){
        clipPath.reset()

        clipRectF.top = 0f
        clipRectF.left = 0f
        clipRectF.right = canvas.width.toFloat()
        clipRectF.bottom = canvas.height.toFloat()
        clipPath.addRoundRect(clipRectF, cornerRadius, cornerRadius, Path.Direction.CW)

        canvas.clipPath(clipPath)
    }

    open fun drawShadow(canvas: Canvas) {
        canvas.save()
        shadowPaint.isAntiAlias = true
        shadowPaint.style = Paint.Style.STROKE
        shadowPaint.color = shadowColor
        shadowPaint.strokeWidth = shadowStrokeWidth
        shadowPaint.xfermode = porterDuffXfermode

        shadowPaint.maskFilter = blurMaskFilter

        shadowPath.reset()
        shadowPath.moveTo((width + xOffsetTopRight), yOffsetTopRight)                                     //TR
        shadowPath.lineTo((xOffsetTopLeft), yOffsetTopLeft)                                           //TR-->TL
        shadowPath.lineTo((xOffsetBottomLeft), (height + yOffsetBottomLeft))                //TL-->BL
        shadowPath.lineTo((width + xOffsetBottomRight), (height + yOffsetBottomRight))          //BL-->BR
        shadowPath.lineTo((width + xOffsetTopRight), yOffsetTopRight)                                 //BR-->TR

        canvas.drawPath(shadowPath, shadowPaint)
        canvas.restore()
    }
}