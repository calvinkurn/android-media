package com.tokopedia.rechargeocr.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.view.View

/**
 * @author by jessica on 10/06/21
 */
class FocusCameraView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    private var mTransparentPaint: Paint = Paint()
    private var mSemiBlackPaint: Paint = Paint()
    private val mPath = Path()

    init {
        initPaints()
    }

    private fun initPaints() {
        mTransparentPaint.color = Color.TRANSPARENT
        mTransparentPaint.strokeWidth = CONST_STROKE_WIDTH.toFloat()
        mSemiBlackPaint.color = Color.TRANSPARENT
        mSemiBlackPaint.strokeWidth = CONST_STROKE_WIDTH.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mPath.reset()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mPath.addRect(left + (right - left) / LEFT_DIMEN_DIVIDER.toFloat(),
                    (top + (bottom - top) / TOP_DIMEN_DIVIDER).toFloat(),
                    right - (right - left) / RIGHT_DIMEN_DIVIDER.toFloat(),
                    (bottom - (bottom - top) / BOTTOM_DIMEN_DIVIDER).toFloat(),
                    Path.Direction.CW)
        } else {
            mPath.addRoundRect(
                    left + (right - left) / LEFT_DIMEN_DIVIDER.toFloat(),
                    (top + (bottom - top) / TOP_DIMEN_DIVIDER).toFloat(),
                    right - (right - left) / RIGHT_DIMEN_DIVIDER.toFloat(),
                    (bottom - (bottom - top) / BOTTOM_DIMEN_DIVIDER).toFloat(),
                    CONST_RADIUS.toFloat(),
                    CONST_RADIUS.toFloat(),
                    Path.Direction.CW
            )
        }
        mPath.fillType = Path.FillType.INVERSE_EVEN_ODD

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            canvas!!.drawRect(left + (right - left) / LEFT_DIMEN_DIVIDER.toFloat(),
                    (top + (bottom - top) / TOP_DIMEN_DIVIDER).toFloat(),
                    right - (right - left) / RIGHT_DIMEN_DIVIDER.toFloat(),
                    (bottom - (bottom - top) / BOTTOM_DIMEN_DIVIDER).toFloat(),
                    mTransparentPaint)
        } else {
            canvas!!.drawRoundRect(left + (right - left) / LEFT_DIMEN_DIVIDER.toFloat(),
                    (top + (bottom - top) / TOP_DIMEN_DIVIDER).toFloat(),
                    right - (right - left) / RIGHT_DIMEN_DIVIDER.toFloat(),
                    (bottom - (bottom - top) / BOTTOM_DIMEN_DIVIDER).toFloat(),
                    CONST_RADIUS.toFloat(),
                    CONST_RADIUS.toFloat(),
                    mTransparentPaint)
        }

        canvas.drawPath(mPath, mSemiBlackPaint)
        canvas.clipPath(mPath)
        canvas.drawColor(Color.parseColor("#a642b549"))
    }

    companion object {
        private const val LEFT_DIMEN_DIVIDER = 20
        private const val TOP_DIMEN_DIVIDER = 3.22
        private const val RIGHT_DIMEN_DIVIDER = 20
        private const val BOTTOM_DIMEN_DIVIDER = 2.96
        private const val CONST_RADIUS = 20
        private const val CONST_STROKE_WIDTH = 10
    }
}