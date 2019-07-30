package com.tokopedia.hotel.common.presentation.widget

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import com.tokopedia.hotel.R

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

    init {

        setLayerType(View.LAYER_TYPE_HARDWARE, null)

        val attrs = context.obtainStyledAttributes(attrs, R.styleable.TicketView)
        try {
            circleRadius = attrs.getDimension(R.styleable.TicketView_tv_circleRadius, getDp(DEFAULT_RADIUS).toFloat())
            anchorViewId1 = attrs.getResourceId(R.styleable.TicketView_tv_anchor1, NO_VALUE)
            anchorViewId2 = attrs.getResourceId(R.styleable.TicketView_tv_anchor2, NO_VALUE)
            circleSpace = attrs.getDimension(R.styleable.TicketView_tv_circleSpace, getDp(15f).toFloat())
        } finally {
            attrs.recycle()
        }

        eraser.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
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

        if (view2 != null) {
            view2.getDrawingRect(rect)
            offsetDescendantRectToMyCoords(view2, rect)
            circlePosition2 = rect.bottom.toFloat()
        }

        postInvalidate()
    }

    override fun drawChild(canvas: Canvas?, child: View?, drawingTime: Long): Boolean {
        val drawChild = super.drawChild(canvas, child, drawingTime)
        canvas?.run { drawHoles(this) }
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
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                    } else {
                        viewTreeObserver.removeGlobalOnLayoutListener(this)
                    }
                    setAnchor(anchorView1, null)
                }
            })
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawHoles(canvas)
        super.onDraw(canvas)
    }

    private fun drawHoles(canvas: Canvas) {
        circlesPath = Path()
        val w = width

        if (layoutParams is MarginLayoutParams) {
            val lp = layoutParams as MarginLayoutParams
        }

        // add holes on the ticketView by erasing them
        with(circlesPath) {
            //anchor1
            addCircle(-circleRadius / 4, circlePosition1, circleRadius, Path.Direction.CW) // bottom left hole
            addCircle(w + circleRadius / 4, circlePosition1, circleRadius, Path.Direction.CW)// bottom right hole
        }

        with(canvas) {
            drawPath(circlesPath, eraser)
        }
    }


    private fun getDp(value: Float): Int {
        return when (value) {
            0f -> 0
            else -> {
                val density = resources.displayMetrics.density
                Math.ceil((density * value).toDouble()).toInt()
            }
        }
    }


}