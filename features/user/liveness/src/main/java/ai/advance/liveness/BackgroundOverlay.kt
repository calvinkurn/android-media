package ai.advance.liveness

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

class BackgroundOverlay : View {

    private var mTransparentPaint: Paint? = null
    private var mSemiBlackPaint: Paint? = null
    private var mStatusPaint: Paint? = null
    private val mPath = Path()
    private var statusColor: Boolean = false

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

        mTransparentPaint = Paint()
        mTransparentPaint?.color = Color.TRANSPARENT
        mTransparentPaint?.strokeWidth = strokeWidthDp

        mSemiBlackPaint = Paint()
        mSemiBlackPaint?.color = Color.TRANSPARENT
        mSemiBlackPaint?.strokeWidth = strokeWidthDp

        mStatusPaint = Paint()
        mStatusPaint?.style = Paint.Style.STROKE
        mStatusPaint?.color = Color.WHITE
        mStatusPaint?.strokeWidth = borderStrokeWidthDp
    }

    fun changeColor() {
        statusColor = true
        pickColor()

        val successThread = Thread {
            try {
                Thread.sleep(1000)
                statusColor = false
                pickColor()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        successThread.start()
    }

    private fun pickColor() {
        if (!statusColor) {
            mStatusPaint?.color = Color.WHITE
        } else {
            mStatusPaint?.color = Color.GREEN
        }
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mPath.reset()

        val width = width
        val height = height

        val radius: Float
        radius = if (width < height)
            width / 2.7f
        else
            height / 2.7f

        mPath.addCircle((right / 2).toFloat(),
                (bottom / 3).toFloat(),
                radius,
                Path.Direction.CW)  //invisible circle
        mPath.fillType = Path.FillType.INVERSE_EVEN_ODD

        mSemiBlackPaint?.let { canvas.drawPath(mPath, it) }
        canvas.clipPath(mPath)
        canvas.drawColor(Color.parseColor("#ae000000"))

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
