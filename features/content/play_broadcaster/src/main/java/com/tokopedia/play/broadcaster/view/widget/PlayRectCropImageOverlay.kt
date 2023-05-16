package com.tokopedia.play.broadcaster.view.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.play.broadcaster.R

/**
 * @author by furqan on 03/06/2020
 */
class PlayRectCropImageOverlay @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : AppCompatImageView(context, attrs, defStyleAttr) {

    private var mTransparentPaint: Paint = Paint()
    private var mSemiPaint: Paint
    private var mBlackTransparentPaint: Paint
    private var mPath = Path()

    private var leftPosition: Float = 0f
    private var topPosition: Float = 0f
    private var rightPosition: Float = 0f
    private var bottomPosition: Float = 0f

    init {
        mTransparentPaint.color = Color.TRANSPARENT
        mTransparentPaint.strokeWidth = STROKE_WIDTH

        mSemiPaint = Paint()
        mSemiPaint.color = Color.TRANSPARENT
        mSemiPaint.strokeWidth = STROKE_WIDTH

        mBlackTransparentPaint = Paint()
        mBlackTransparentPaint.color = resources.getColor(R.color.play_dms_N700_68)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mPath.reset()

        val rectHeight = height.toFloat()
        val rectWidth = height.toFloat() * 9 / 16

        leftPosition = left + (((right - left).toFloat() / 2) - (rectWidth / 2))
        topPosition = 0f
        rightPosition = right - ((right - left) / 2) + (rectWidth / 2)
        bottomPosition = rectHeight

        mPath.addRoundRect(leftPosition,
                topPosition,
                rightPosition,
                bottomPosition,
                CENTER_RECT_RADIUS,
                CENTER_RECT_RADIUS,
                Path.Direction.CW)
        mPath.fillType = Path.FillType.INVERSE_EVEN_ODD

        // draw transparent center rect
        canvas.drawRoundRect(leftPosition,
                topPosition,
                rightPosition,
                bottomPosition,
                CENTER_RECT_RADIUS,
                CENTER_RECT_RADIUS,
                mTransparentPaint)

        canvas.drawPath(mPath, mSemiPaint)
        canvas.clipPath(mPath)
        canvas.drawColor(resources.getColor(R.color.play_dms_cover_crop_overlay))
    }

    fun getCropRect(): RectF =
            RectF(leftPosition, topPosition, rightPosition, bottomPosition)

    companion object {
        private const val CENTER_RECT_RADIUS = 20f
        private const val STROKE_WIDTH = 10f
    }
}
