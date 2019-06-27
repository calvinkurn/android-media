package com.tokopedia.graphview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.Path
import android.graphics.Rect
import android.graphics.Shader
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View

import java.util.ArrayList

/**
 * View that shows graph
 */
open class LineChartView : View {

    private var datapoints: List<Float>? = null             //Points to be plotted
    private var constantRange: FloatArray? = null              //Divides datapoints provided into equidistant poonts
    private var xAxisCumulativeDataWidth: FloatArray? = null    // Store cumulative width of X-Data Strings in pixels
    private var xData: List<String>? = null
    private var defaultLineAndPointColor = Color.parseColor("#64caff")
    private var minDataValue: Float = 0.toFloat()
    private var maxDataValue: Float = 0.toFloat()
    private var lineColor: Int = 0
    private var showPoints: Boolean = false
    private var showShadowGradient: Boolean = false
    private var showBackgroundLines: Boolean = false
    private var showLines: Boolean = false
    private var showPopupWindow: Boolean = false
    private var lineWidth: Int = 0
    private var pointColor: Int = 0
    private var pointRadius: Int = 0
    private var backgroundLinesColor: Int = 0
    private var gradientStartColor: Int = 0
    private var gradientEndColor: Int = 0
    private var axisTextColor: Int = 0
    private var axisTextSize: Float = 0.toFloat()
    private var backgroundLinesWidth: Int = 0
    private var relativeXShift: Float = 0.toFloat()                     //Width of maximum text to be plotted on Y-Axis
    private var relativeYTextShift: Float = 0.toFloat()                 //Height of text Plotted on X-Axis
    private var backLinePaint = Paint()                                 //background lines paint
    private var axisTextPaint = Paint()
    private var linePaint = Paint()                                     //Paint of chart Line
    private var gradientPaint = Paint()                                 //Point of the drop gradient
    private var circlePaint = Paint()                                   // Paint of the point
    private var xMaxDataLength: Float = 0.toFloat()                     //width required in pixels by maximum text that is to be plotted on x-axis
    private var yDataPointsCount: Int = 0                               //Number of points/Lines that can be plotted on Y-axis within given Height
    private var leftMargin: Float = 0.toFloat()                  // Left margin of View
    private var rightMargin: Float = 0.toFloat()                    // Right margin of View
    private var xAxisBackgroundLineandTextMargin: Float = 0.toFloat()        //Margin between yMaximumData and background lines
    private var topMargin: Float = 0.toFloat()                   // Top margin of View
    private var bottomMargin: Float = 0.toFloat()                    // Bottom margin of View
    private var xIntervalMargin: Float = 0.toFloat()                //Margin between adjacent poins that are to be plotted on x-axis
    private var xDataCount: Int = 0                                    //xData count
    private var yDataCount: Int = 0                                     //datapoints count
    private var dataCoordinatesList: ArrayList<DataCoordinates>? = null     //Stores mapping of each x and y datapoints
    private var lineChartBasePopupWindow: LineChartBasePopupWindow? = null
    private var pointTouchableAreaRadius: Float = 0.toFloat()           //Radius of point where touch will be intercepted

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }


    /**
     * Call this method to create graph
     * @param datapoints Points to be plotted on y-axis
     * @param xData y-axis mapping textual info to be shown on x-axis
     * Both params should be of same size
     *
     */
    fun setChartData(datapoints: List<Float>, xData: List<String>) {
        this.datapoints = ArrayList(datapoints)
        this.xData = ArrayList(xData)
        xDataCount = xData.size
        yDataCount = datapoints.size
        if (xDataCount != yDataCount) {
            throw IllegalArgumentException("Please provide both inputs of same size.")
        }
        calculateCumulativeDataWidth()
        minDataValue = getMin(datapoints)
        maxDataValue = getMax(datapoints)
        setYDataPoints()
        xMaxDataLength = maxXDataLength
        relativeXShift = maxYDataLength
        val bounds = Rect()
        axisTextPaint.getTextBounds(constantRange!![0].toString(), 0, 5, bounds)
        relativeYTextShift = bounds.height().toFloat()
        invalidate()
    }


    /**
     * Calculates Cumulative width of all texts to be drawn on x-axis
     */
    private fun calculateCumulativeDataWidth() {
        xAxisCumulativeDataWidth = FloatArray(xDataCount)
        xAxisCumulativeDataWidth!![0] = axisTextPaint.measureText(xData?.get(0))
        for (i in 1 until xDataCount) {
            xAxisCumulativeDataWidth!![i] = xAxisCumulativeDataWidth!![i - 1] + axisTextPaint.measureText(xData?.get(i))
        }
    }

    /**
     * Calculate the width required in pixels by maximum text that is to be plotted on x-axis
     */
    private var maxXDataLength: Float = 0.toFloat()
        get() {
            if (xAxisCumulativeDataWidth != null && xAxisCumulativeDataWidth!!.size > 0) {
                var maxLength = xAxisCumulativeDataWidth!![0]
                for (i in 1 until xAxisCumulativeDataWidth!!.size) {

                    val length = xAxisCumulativeDataWidth!![i] - xAxisCumulativeDataWidth!![i - 1]
                    if (length > maxLength) {
                        maxLength = length
                    }
                }
                return maxLength
            }
            return -1f
        }


    /**
     * Calculate the width required in pixels by maximum text that is to be plotted on y-axis
     * (Used to calculate available space in which graph can be plotted)
     */
    private val maxYDataLength: Float
        get() {
            if (constantRange != null && constantRange!!.size > 0) {
                var maxLength = axisTextPaint.measureText(constantRange!![0].toString())
                for (i in constantRange!!.indices) {
                    val length = axisTextPaint.measureText(constantRange!![i].toString())
                    if (length > maxLength) {
                        maxLength = length
                    }
                }
                return maxLength
            }
            return -1f
        }


    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LineChartView)
            showPoints = typedArray.getBoolean(R.styleable.LineChartView_lcv_showPoints, true)
            showShadowGradient = typedArray.getBoolean(R.styleable.LineChartView_lcv_showShadowGradient, false)
            showBackgroundLines = typedArray.getBoolean(R.styleable.LineChartView_lcv_showBackgroundLines, true)
            showLines = typedArray.getBoolean(R.styleable.LineChartView_lcv_showLines, true)
            showPopupWindow = typedArray.getBoolean(R.styleable.LineChartView_lcv_showPopupWindow, true)
            lineColor = typedArray.getColor(R.styleable.LineChartView_lcv_lineColor, defaultLineAndPointColor)
            lineWidth = typedArray.getDimensionPixelSize(R.styleable.LineChartView_lcv_lineWidth, dpToPx(2f, context))
            pointColor = typedArray.getColor(R.styleable.LineChartView_lcv_pointColor, defaultLineAndPointColor)
            pointRadius = typedArray.getDimensionPixelSize(R.styleable.LineChartView_lcv_pointRadius, dpToPx(3f, context))
            backgroundLinesWidth = typedArray.getDimensionPixelSize(R.styleable.LineChartView_lcv_backgroundLinesWidth, dpToPx(1f, context))
            backgroundLinesColor = typedArray.getColor(R.styleable.LineChartView_lcv_backgroundLinesColor, Color.parseColor("#e0e0e0"))
            gradientStartColor = typedArray.getColor(R.styleable.LineChartView_lcv_gradientStartColor, Color.parseColor("#b398dcff"))
            gradientEndColor = typedArray.getColor(R.styleable.LineChartView_lcv_gradientEndColor, Color.parseColor("#b3ffffff"))
            axisTextSize = typedArray.getDimension(R.styleable.LineChartView_lcv_axisTextSize, dpToPx(12f, context).toFloat())
            axisTextColor = typedArray.getColor(R.styleable.LineChartView_lcv_axisTextColor, Color.BLACK)
            pointTouchableAreaRadius = typedArray.getDimension(R.styleable.LineChartView_lcv_pointTouchableAreaRadius, dpToPx(30f, context).toFloat())
            yDataPointsCount = typedArray.getInteger(R.styleable.LineChartView_lcv_yPointsCount, 8)
            leftMargin = dpToPx(resources.getDimension(R.dimen.lcv_left_margin), context).toFloat()
            rightMargin = dpToPx(resources.getDimension(R.dimen.lcv_right_margin), context).toFloat()
            topMargin = dpToPx(resources.getDimension(R.dimen.lcv_top_margin), context).toFloat()
            bottomMargin = dpToPx(resources.getDimension(R.dimen.lcv_bottom_margin), context).toFloat()
            xAxisBackgroundLineandTextMargin = dpToPx(resources.getDimension(R.dimen.lcv_left_margin_from_text), context).toFloat()
            initBackgroundLinePaint()
            initAxisTextPaint()
            initLinePaint()
            initCirclePaint()

        }
        lineChartBasePopupWindow = LineChartBasePopupWindow(context)
    }

    override fun onDraw(canvas: Canvas) {
        drawBackground(canvas)
        drawLineChart(canvas)

    }

    private fun initBackgroundLinePaint() {
        backLinePaint.style = Style.STROKE
        backLinePaint.strokeWidth = backgroundLinesWidth.toFloat()
        backLinePaint.color = backgroundLinesColor
    }

    private fun initLinePaint() {
        linePaint.style = Style.STROKE
        linePaint.strokeWidth = lineWidth.toFloat()
        linePaint.color = lineColor
        linePaint.isAntiAlias = true
    }

    private fun initGradientPaint() {
        gradientPaint.shader = LinearGradient(0f, 0f, 0f, height.toFloat(),
                gradientStartColor, gradientEndColor, Shader.TileMode.MIRROR)
    }

    private fun initAxisTextPaint() {
        axisTextPaint.style = Style.FILL_AND_STROKE
        axisTextPaint.textSize = axisTextSize
        axisTextPaint.color = axisTextColor
    }

    private fun initCirclePaint() {
        circlePaint.color = pointColor
        circlePaint.isAntiAlias = true
        circlePaint.style = Style.FILL
    }

    /**
     * Draws background lines text on y-axis
     */

    private fun drawBackground(canvas: Canvas) {
        for (count in constantRange!!.indices) {
            val yPos = getYPosNew(count.toFloat())
            canvas.drawText(constantRange!![count].toString(), leftMargin, yPos + relativeYTextShift / 2.0f, axisTextPaint)     // x given left margin and y shifts text in between of background line drawn
            if (showBackgroundLines)
                canvas.drawLine(getXPos(0) - xAxisBackgroundLineandTextMargin / 2.0f, //startx for line startes from little before
                        yPos,
                        width.toFloat() - paddingRight.toFloat() - rightMargin, //
                        yPos, backLinePaint)

        }
    }


    /**
     * Finds division of datapoints to be drawn on y-axis according to number of dataPoints
     */
    private fun setYDataPoints() {
        val max = Math.ceil(maxDataValue.toDouble()).toInt()
        val min = Math.floor(minDataValue.toDouble()).toInt()
        var diffRange = Math.ceil(((max - min).toFloat() / (yDataPointsCount - 1)).toDouble()).toInt()
        if (diffRange == 0) {
            diffRange = 1
        }
        constantRange = FloatArray(yDataPointsCount)
        for (count in 0 until yDataPointsCount) {
            constantRange!![count] = minDataValue + diffRange * count
        }

    }


    /**
     * Draws Line chart, Points, Line and x-data
     */
    private fun drawLineChart(canvas: Canvas) {
        calculateXandYMapping()
        val path = Path()
        var i: Int = 0
        var xCurrentDataLength = 0f
        if (showLines) {
            while (i < dataCoordinatesList!!.size) {
                if (i == 0) {
                    xCurrentDataLength = xAxisCumulativeDataWidth!![0]
                    path.moveTo(dataCoordinatesList!![i].x + xCurrentDataLength / 2.0f, dataCoordinatesList!![i].y)
                } else {
                    xCurrentDataLength = xAxisCumulativeDataWidth!![i] - xAxisCumulativeDataWidth!![i - 1]
                    path.lineTo(dataCoordinatesList!![i].x + xCurrentDataLength / 2.0f, dataCoordinatesList!![i].y)

                }
                i++
            }
            canvas.drawPath(path, linePaint)       //draw graph lines
        }


        if (showShadowGradient) {               //draws gradient top to bottom
            var xLastDataLength = 0.0f
            if (i == 0)
                xLastDataLength = xAxisCumulativeDataWidth!![0]
            else
                xLastDataLength = xAxisCumulativeDataWidth!![xDataCount - 1] - xAxisCumulativeDataWidth!![xDataCount - 2]
            initGradientPaint()
            path.lineTo(getXPos(xDataCount - 1) + xLastDataLength / 2.0f,
                    height.toFloat() - paddingBottom.toFloat() - relativeYTextShift - bottomMargin)
            path.lineTo(getXPos(0) + xAxisCumulativeDataWidth!![0] / 2, height.toFloat() - paddingBottom.toFloat() - relativeYTextShift - bottomMargin)
            path.lineTo(getXPos(0) + xAxisCumulativeDataWidth!![0] / 2, getYPosNew(0f))
            canvas.drawPath(path, gradientPaint)
        }

        i = 0
        while (i < dataCoordinatesList!!.size) {
            if (i == 0)
                xCurrentDataLength = xAxisCumulativeDataWidth!![0]
            else
                xCurrentDataLength = xAxisCumulativeDataWidth!![i] - xAxisCumulativeDataWidth!![i - 1]
            if (showPoints)
                canvas.drawCircle(dataCoordinatesList!![i].x + xCurrentDataLength / 2.0f, dataCoordinatesList!![i].y, pointRadius.toFloat(), circlePaint)          //draws circle points
            if (!TextUtils.isEmpty(xData!![i])) {
                canvas.drawText(xData!![i],
                        dataCoordinatesList!![i].x,
                        height - relativeYTextShift / 3.0f,
                        axisTextPaint)
            }
            i++
        }


    }

    /**
     * Calculates mapping of each x and y coordinates
     */
    private fun calculateXandYMapping() {
        var x = 0f
        var y1 = 0f
        var y2 = 0f
        var relativeYPos = 0f
        var y = 0f
        dataCoordinatesList = ArrayList()
        var i = 0
        var j = 0
        i = 0
        while (i < datapoints!!.size) {
            j = 0
            while (j < constantRange!!.size - 1) {
                if (datapoints!![i] >= constantRange!![j] && datapoints!![i] <= constantRange!![j + 1]) {
                    break
                }
                j++
            }
            if (j < constantRange!!.size - 1) {
                y1 = getYPosNew(j.toFloat())
                y2 = getYPosNew((j + 1).toFloat())
                relativeYPos = y1 - y2 - (datapoints!![i] - constantRange!![j]) / (constantRange!![j + 1] - constantRange!![j]) * (y1 - y2)
                x = getXPos(i)
                y = y2 + relativeYPos

                dataCoordinatesList!!.add(DataCoordinates(x, y))
            }
            i++
        }
    }

    fun setPopupWindow(popupWindowLineChart: LineChartBasePopupWindow) {
        this.lineChartBasePopupWindow = popupWindowLineChart
    }


    /**
     * To show Popup-Window if point is tapped
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (showPopupWindow) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                return true
            } else if (event.action == MotionEvent.ACTION_UP) {
                var i = 0
                for (dataCoordinates in dataCoordinatesList!!) {
                    if (dataCoordinates.x - pointTouchableAreaRadius < event.x && event.x < dataCoordinates.x + pointTouchableAreaRadius
                            &&
                            dataCoordinates.y - pointTouchableAreaRadius < event.y && event.y < dataCoordinates.y + pointTouchableAreaRadius) {
                        showPopupWindowOnTop(dataCoordinates.x.toInt(), dataCoordinates.y.toInt(), datapoints!![i])
                        break
                    }
                    i++
                }
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * Shows Popup Window
     */
    private fun showPopupWindowOnTop(x: Int, y: Int, yData: Float?) {
        val globalLocation = Rect()
        val localLocation = Rect()
        getGlobalVisibleRect(globalLocation)
        getLocalVisibleRect(localLocation)
        lineChartBasePopupWindow!!.setPopupTitleText(yData.toString())
        lineChartBasePopupWindow!!.showAtLocation(this, Gravity.NO_GRAVITY, x - localLocation.left, y + globalLocation.top)
    }

    private fun getMin(array: List<Float>): Float {
        var max = array[0]
        for (i in 1 until array.size) {
            if (array[i] < max) {
                max = array[i]
            }
        }
        return max
    }

    private fun getMax(array: List<Float>): Float {
        var max = array[0]
        for (i in 1 until array.size) {
            if (array[i] > max) {
                max = array[i]
            }
        }
        return max
    }

    /**
     * Sets Width of View According to data to be plotted, Increases width automatically if view wouldn't fit in given Frame.
     */

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //        sup   er.onMeasure(widthMeasureSpec, heightMeasureSpec);
        val viewWidth = View.MeasureSpec.getSize(widthMeasureSpec).toFloat()
        val availableWidth = viewWidth - leftMargin - paddingLeft.toFloat() - rightMargin - paddingRight.toFloat() - relativeXShift - xAxisBackgroundLineandTextMargin
        val acquiredWidthByData = xAxisCumulativeDataWidth!![xDataCount - 1]
        var widthNeeded = xDataCount * xMaxDataLength
        val measuredWidth: Float
        if (widthNeeded > availableWidth)
            measuredWidth = widthNeeded + paddingLeft.toFloat() + paddingRight.toFloat() + relativeXShift + xAxisBackgroundLineandTextMargin + leftMargin + rightMargin
        else {
            measuredWidth = viewWidth
            widthNeeded = availableWidth
        }
        xIntervalMargin = (widthNeeded - acquiredWidthByData) / (xDataCount - 1)
        setMeasuredDimension(measuredWidth.toInt(), heightMeasureSpec)
    }


    /**
     * Find the position on Y-Axis where i'th point will lie in the given height
     * @param value i'th point
     */
    private fun getYPosNew(value: Float): Float {
        var value = value
        val height = (height.toFloat()
                - paddingTop.toFloat()
                - paddingBottom.toFloat()
                - 2 * relativeYTextShift
                - bottomMargin
                - topMargin)


        value = value / (yDataPointsCount - 1) * height

        value = height - value

        value += paddingTop.toFloat() + relativeYTextShift + topMargin

        return value
    }

    /**
     * Find the poisition on X-Axis where i'th point will lie in the given height
     * @param value i'th point
     */
    private fun getXPos(value: Int): Float {

        var cumulativePrevTextWidths = 0.0f
        if (value > 0 && value < xDataCount) {
            cumulativePrevTextWidths = xAxisCumulativeDataWidth!![value - 1]
        }


        return paddingLeft.toFloat() + leftMargin + relativeXShift + xAxisBackgroundLineandTextMargin + value * xIntervalMargin + cumulativePrevTextWidths
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
        return px.toInt()
    }


    fun setLineColor(lineColor: Int) {
        this.lineColor = lineColor
        initLinePaint()
    }

    fun setShowPoints(showPoints: Boolean) {
        this.showPoints = showPoints
    }

    fun setShowShadowGradient(showShadowGradient: Boolean) {
        this.showShadowGradient = showShadowGradient
    }

    fun setShowBackgroundLines(showBackgroundLines: Boolean) {
        this.showBackgroundLines = showBackgroundLines
    }

    fun setLineWidth(lineWidth: Int) {
        this.lineWidth = lineWidth
        initLinePaint()
    }

    fun setPointColor(pointColor: Int) {
        this.pointColor = pointColor
        initCirclePaint()
    }

    fun setPointRadius(pointRadius: Int) {
        this.pointRadius = pointRadius
        initCirclePaint()
    }

    fun setBackgroundLinesColor(backgroundLinesColor: Int) {
        this.backgroundLinesColor = backgroundLinesColor
        initBackgroundLinePaint()
    }

    fun setGradientStartColor(gradientStartColor: Int) {
        this.gradientStartColor = gradientStartColor
    }

    fun setGradientEndColor(gradientEndColor: Int) {
        this.gradientEndColor = gradientEndColor
    }

    fun setYDataPointsCount(yDataPointsCount: Int) {
        this.gradientEndColor = yDataPointsCount
    }

}