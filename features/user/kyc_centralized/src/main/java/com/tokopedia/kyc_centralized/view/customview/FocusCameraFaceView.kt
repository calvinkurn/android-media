package com.tokopedia.kyc_centralized.view.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker

/**
 * @author by alvinatin on 07/11/18.
 */
class FocusCameraFaceView : View {
    private val LEFT_OVAL_DIMEN_DIVIDER = 1.3f
    private val TOP_OVAL_DIMEN_DIVIDER = 1.7
    private val RIGHT_OVAL_DIMEN_DIVIDER = 1.3f
    private val BOTTOM_OVAL_DIMEN_DIVIDER = 1.25
    private var mTransparentPaint: Paint? = null
    private var mSemiBlackPaint: Paint? = null
    private val mPath = Path()

    constructor(context: Context?) : super(context) {
        initPaints()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initPaints()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initPaints()
    }

    private fun initPaints() {
        mTransparentPaint = Paint()
        mTransparentPaint?.color = Color.TRANSPARENT
        mTransparentPaint?.strokeWidth = CONST_STROKE_WIDTH.toFloat()
        mSemiBlackPaint = Paint()
        mSemiBlackPaint?.color = Color.TRANSPARENT
        mSemiBlackPaint?.strokeWidth = CONST_STROKE_WIDTH.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPath.reset()
        mPath.addRoundRect(
                left + (right - left) / LEFT_DIMEN_DIVIDER,
                (top + (bottom - top) / TOP_DIMEN_DIVIDER).toFloat(),
                right - (right - left) / RIGHT_DIMEN_DIVIDER,
                (bottom - (bottom - top) / BOTTOM_DIMEN_DIVIDER).toFloat(),
                CONST_RADIUS.toFloat(),
                CONST_RADIUS.toFloat(),
                Path.Direction.CW
        )
        mPath.addOval(left + (right - left) / LEFT_OVAL_DIMEN_DIVIDER,
                (top + (bottom - top) / TOP_OVAL_DIMEN_DIVIDER).toFloat(),
                right - (right - left) / RIGHT_OVAL_DIMEN_DIVIDER,
                (bottom - (bottom - top) / BOTTOM_OVAL_DIMEN_DIVIDER).toFloat(),
                Path.Direction.CW)

        mPath.fillType = Path.FillType.INVERSE_EVEN_ODD

        mTransparentPaint?.let {
            canvas.drawRoundRect(left + (right - left) / LEFT_DIMEN_DIVIDER,
                    (top + (bottom - top) / TOP_DIMEN_DIVIDER).toFloat(),
                    right - (right - left) / RIGHT_DIMEN_DIVIDER,
                    (bottom - (bottom - top) / BOTTOM_DIMEN_DIVIDER).toFloat(),
                    CONST_RADIUS.toFloat(),
                    CONST_RADIUS.toFloat(),
                    it)
        }
        mTransparentPaint?.let {
            canvas.drawOval(left + (right - left) / LEFT_OVAL_DIMEN_DIVIDER,
                    (top + (bottom - top) / TOP_OVAL_DIMEN_DIVIDER).toFloat(),
                    right - (right - left) / RIGHT_OVAL_DIMEN_DIVIDER,
                    (bottom - (bottom - top) / BOTTOM_OVAL_DIMEN_DIVIDER).toFloat(),
                    it)
        }

        mSemiBlackPaint?.let { canvas.drawPath(mPath, it) }
        canvas.clipPath(mPath)
        canvas.drawColor(MethodChecker.getColor(this.context, com.tokopedia.user_identification_common.R.color.kyc_dms_overlay))
    }

    companion object {
        private const val LEFT_DIMEN_DIVIDER = 1.4f
        private const val TOP_DIMEN_DIVIDER = 1.25
        private const val RIGHT_DIMEN_DIVIDER = 1.4f
        private const val BOTTOM_DIMEN_DIVIDER = 2.75
        private const val CONST_RADIUS = 20
        private const val CONST_STROKE_WIDTH = 10
    }
}