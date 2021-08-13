package com.tokopedia.mvcwidget.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.tokopedia.promoui.common.R
import com.tokopedia.promoui.common.dpToPx

class MvcShadowLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseShadowLayout(context, attrs, defStyleAttr) {

    var xOffsetTopRight = -dpToPx(9)           //SeekBarOne
    var xOffsetTopLeft = -xOffsetTopRight
    var xOffsetBottomRight = -dpToPx(7)        //SeekBarTwo
    var xOffsetBottomLeft = -xOffsetBottomRight


    var yOffsetTopRight = dpToPx(9)          //SeekBarThree
    var yOffsetTopLeft = yOffsetTopRight
    var yOffsetBottomLeft = -dpToPx(8)                          //SeekBarFour
    var yOffsetBottomRight = yOffsetBottomLeft

    override fun readAttrs(attrs: AttributeSet?) {
        super.readAttrs(attrs)
        enableShadow = true
        shadowStrokeWidth = dpToPx(3)
        blurRadius = dpToPx(8)
        shadowColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32)
    }

    override fun drawShadow(canvas: Canvas) {
        canvas.save()
        shadowPaint.isAntiAlias = true
        shadowPaint.style = Paint.Style.STROKE
        shadowPaint.color = shadowColor
        shadowPaint.strokeWidth = shadowStrokeWidth
        shadowPaint.xfermode = porterDuffXfermode

        shadowPaint.maskFilter = blurMaskFilter
        xOffsetTopLeft = - xOffsetTopRight
        xOffsetBottomLeft = - xOffsetBottomRight
        yOffsetTopLeft = yOffsetTopRight
        yOffsetBottomRight = yOffsetBottomLeft

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