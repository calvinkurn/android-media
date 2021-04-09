package com.tokopedia.liveness.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.liveness.R
import java.lang.Exception

class BackgroundOverlay : View {

    private var mSemiBlackPaint: Paint? = null
    private var mStatusPaint: Paint? = null
    private val mPath = Path()
    private var statusColor: Boolean = false
    private var successThread: Thread? = null

    constructor(context: Context) : super(context) {
        initPaints()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initPaints()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initPaints()
    }

    private fun initPaints() {

        val strokeWidthDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CONST_STROKE_WIDTH, resources.displayMetrics)
        val borderStrokeWidthDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CONST_BORDER_STROKE_WIDTH, resources.displayMetrics)

        mSemiBlackPaint = Paint()
        mSemiBlackPaint?.color = Color.TRANSPARENT
        mSemiBlackPaint?.strokeWidth = strokeWidthDp

        mStatusPaint = Paint()
        mStatusPaint?.style = Paint.Style.STROKE
        mStatusPaint?.color = MethodChecker.getColor(context, R.color.liveness_dms_status_false)
        mStatusPaint?.strokeWidth = borderStrokeWidthDp
    }

    fun changeColor() {
        statusColor = true
        pickColor()

        successThread = Thread {
            try {
                Thread.sleep(1000)
                statusColor = false
                pickColor()
                successThread?.interrupt()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        successThread?.start()
    }

    private fun pickColor() {
        if (!statusColor) {
            mStatusPaint?.color = MethodChecker.getColor(context, R.color.liveness_dms_status_false)
        } else {
            mStatusPaint?.color = MethodChecker.getColor(context, R.color.liveness_dms_status_true)
        }
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mPath.reset()

        val width = width
        val height = height

        val radius = if (width < height) {
            width / 2.7f
        } else {
            height / 2.7f
        }

        mPath.addCircle((right / 2).toFloat(),
                (bottom / 3).toFloat(),
                radius,
                Path.Direction.CW)  //invisible circle
        mPath.fillType = Path.FillType.INVERSE_EVEN_ODD

        mSemiBlackPaint?.let { canvas.drawPath(mPath, it) }
        canvas.clipPath(mPath)
        canvas.drawColor(MethodChecker.getColor(context, R.color.liveness_dms_background_overlay))

        mStatusPaint?.let { canvas.drawCircle((right / 2).toFloat(),
                (bottom / 3).toFloat(),
                radius,
                it)
        }

    }

    companion object {
        private const val CONST_STROKE_WIDTH = 1F
        private const val CONST_BORDER_STROKE_WIDTH = 10F
    }
}
