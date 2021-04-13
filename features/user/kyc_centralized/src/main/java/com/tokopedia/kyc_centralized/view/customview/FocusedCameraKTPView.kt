package com.tokopedia.kyc_centralized.view.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kyc_centralized.R
import com.tokopedia.user_identification_common.KYCConstant

/**
 * @author by alvinatin on 06/11/18.
 */
class FocusedCameraKTPView : View {
    private var mSemiBlackPaint: Paint? = null
    private var mWhitePaint: Paint? = null
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
        mSemiBlackPaint = Paint()
        mSemiBlackPaint?.color = Color.TRANSPARENT
        mSemiBlackPaint?.strokeWidth = CONST_STROKE_WIDTH.toFloat()
        mWhitePaint = Paint()
        mWhitePaint?.style = Paint.Style.STROKE
        mWhitePaint?.color = MethodChecker.getColor(context, R.color.kyc_dms_border_camera)
        mWhitePaint?.strokeWidth = CONST_STROKE_WIDTH.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPath.reset()
        mPath.addRoundRect(
                left + (right - left) / LEFT_DIMEN_DIVIDER.toFloat(),
                (top + (bottom - top) / TOP_DIMEN_DIVIDER).toFloat(),
                right - (right - left) / RIGHT_DIMEN_DIVIDER.toFloat(),
                (bottom - (bottom - top) / BOTTOM_DIMEN_DIVIDER).toFloat(),
                CONST_RADIUS.toFloat(),
                CONST_RADIUS.toFloat(),
                Path.Direction.CW
        )
        mPath.fillType = Path.FillType.INVERSE_EVEN_ODD
        mSemiBlackPaint?.let { canvas.drawPath(mPath, it) }
        canvas.clipPath(mPath)
        canvas.drawColor(Color.parseColor(KYCConstant.KYC_OVERLAY_COLOR))
        mWhitePaint?.let {
            canvas.drawRoundRect(left + (right - left) / LEFT_DIMEN_DIVIDER.toFloat(),
                    (top + (bottom - top) / TOP_DIMEN_DIVIDER).toFloat(),
                    right - (right - left) / RIGHT_DIMEN_DIVIDER.toFloat(),
                    (bottom - (bottom - top) / BOTTOM_DIMEN_DIVIDER).toFloat(),
                    CONST_RADIUS.toFloat(),
                    CONST_RADIUS.toFloat(),
                    it)
        }
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