package com.tokopedia.common.travel.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import com.tokopedia.common.travel.R
import kotlin.math.ceil

/**
 * @author by furqan on 14/05/19
 */
class TicketView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : RelativeLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_RADIUS: Float = 9f
        private const val NO_VALUE = -1
    }

    private val eraser = Paint(Paint.ANTI_ALIAS_FLAG)

    private var anchorViewId1: Int = 0
    private var anchorViewId2: Int = 0

    private var circlesPath = Path()
    private var circlePosition1: Float = 0f
    private var circlePosition2: Float = 0f
    private var circleRadius: Float = 0f
    private var circleSpace: Float = 0f

    private var dashColor: Int = 0
    private var dashSize: Float = 0f
    private val dashPath = Path()
    private val dashPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {

        setLayerType(View.LAYER_TYPE_HARDWARE, null)

        val a = context.obtainStyledAttributes(attrs, R.styleable.TicketView)
        try {
            circleRadius = a.getDimension(R.styleable.TicketView_tv_circleRadius, getDp(DEFAULT_RADIUS).toFloat())
            anchorViewId1 = a.getResourceId(R.styleable.TicketView_tv_anchor1, NO_VALUE)
            anchorViewId2 = a.getResourceId(R.styleable.TicketView_tv_anchor2, NO_VALUE)
            circleSpace = a.getDimension(R.styleable.TicketView_tv_circleSpace, getDp(15f).toFloat())
            dashColor = a.getColor(R.styleable.TicketView_tv_dashColor, Color.parseColor("#00000000"))
            dashSize = a.getDimension(R.styleable.TicketView_tv_dashSize, getDp(1.5f).toFloat())
        } finally {
            a.recycle()
        }

        eraser.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        dashPaint.color = dashColor
        dashPaint.style = Paint.Style.STROKE
        dashPaint.strokeWidth = dashSize
        dashPaint.pathEffect = DashPathEffect(floatArrayOf(getDp(3f).toFloat(), getDp(3f).toFloat()), 0f)
    }

    fun setRadius(radius: Float) {
        this.circleRadius = radius
        postInvalidate()
    }

    fun setAnchor(view1: View?, view2: View?) {

        val rect = Rect()
        view1?.getDrawingRect(rect)
        offsetDescendantRectToMyCoords(view1, rect)
        circlePosition1 = rect.bottom.toFloat()

        if (view2 != null && anchorViewId2 != NO_VALUE) {
            view2.getDrawingRect(rect)
            offsetDescendantRectToMyCoords(view2, rect)
            circlePosition2 = rect.bottom.toFloat()
        }

        postInvalidate()
    }

    override fun drawChild(canvas: Canvas?, child: View?, drawingTime: Long): Boolean {
        val drawChild = super.drawChild(canvas, child, drawingTime)
        drawHoles(canvas!!)
        return drawChild
    }

    override fun dispatchDraw(canvas: Canvas?) {
        canvas?.save()
        super.dispatchDraw(canvas)
        canvas?.restore()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (anchorViewId1 != NO_VALUE) {
            val anchorView1 = findViewById<View>(anchorViewId1)
            val anchorView2 = if (anchorViewId2 != NO_VALUE) findViewById<View>(anchorViewId2) else null
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    setAnchor(anchorView1, anchorView2)
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        if (anchorViewId1 != NO_VALUE) {
            val anchorView1 = findViewById<View>(anchorViewId1)
            val anchorView2 = if (anchorViewId2 != NO_VALUE) findViewById<View>(anchorViewId2) else null
            setAnchor(anchorView1, anchorView2)
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawHoles(canvas)
        super.onDraw(canvas)
    }

    private fun drawHoles(canvas: Canvas) {
        circlesPath = Path()
        val w = width

        // add holes on the ticketView by erasing them
        with(circlesPath) {
            //anchor1
            addCircle(-circleRadius / 4, circlePosition1, circleRadius, Path.Direction.CW) // bottom left hole
            addCircle(w + circleRadius / 4, circlePosition1, circleRadius, Path.Direction.CW)// bottom right hole

            //anchor2
            when {
                anchorViewId2 != NO_VALUE -> {
                    addCircle(-circleRadius / 4, circlePosition2, circleRadius, Path.Direction.CW) // bottom left hole
                    addCircle(w + circleRadius / 4, circlePosition2, circleRadius, Path.Direction.CW) // bottom right hole
                }
            }
        }

        with(dashPath) {
            //anchor1
            moveTo(circleRadius, circlePosition1)
            quadTo(w - circleRadius, circlePosition1, w - circleRadius, circlePosition1)

            //anchor2
            when {
                anchorViewId2 != NO_VALUE -> {
                    moveTo(circleRadius, circlePosition2)
                    quadTo(w - circleRadius, circlePosition2, w - circleRadius, circlePosition2)
                }
            }
        }

        with(canvas) {
            if (dashSize > 0)
                drawPath(dashPath, dashPaint)
            drawPath(circlesPath, eraser)
        }
    }


    private fun getDp(value: Float): Int {
        return when (value) {
            0f -> 0
            else -> {
                val density = resources.displayMetrics.density
                ceil((density * value).toDouble()).toInt()
            }
        }
    }
}