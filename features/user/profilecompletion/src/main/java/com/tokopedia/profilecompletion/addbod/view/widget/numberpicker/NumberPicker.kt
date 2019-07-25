package com.tokopedia.profilecompletion.addbod.view.widget.numberpicker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.widget.OverScroller
import com.tokopedia.profilecompletion.R
import java.util.*

/**
 * Created by Ade Fulki on 2019-07-16.
 * ade.hadian@tokopedia.com
 */

class NumberPicker @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val TAG = NumberPicker::class.java.simpleName

    private var selectorItemCount: Int
    private var selectorVisibleItemCount: Int
    private var minIndex: Int
    private var maxIndex: Int

    private var wheelMiddleItemIndex: Int
    private var wheelVisibleItemMiddleIndex: Int
    private var selectorItemIndices: ArrayList<Int>
    private var curSelectedItemIndex = 0
    private var wrapSelectorWheelPreferred: Boolean

    private var textPaint: Paint? = null
    private var selectedTextColor: Int
    private var unSelectedTextColor: Int
    private var textSize: Int
    private var textAlign: String

    private var overScroller: OverScroller? = null
    private var velocityTracker: VelocityTracker? = null
    private val touchSlop: Int
    private val maximumVelocity: Int
    private val minimumVelocity: Int
    private var lastY: Float = 0f
    private var isDragging: Boolean = false
    private var currentFirstItemOffset: Int = 0
    private var initialFirstItemOffset = Int.MIN_VALUE
    private var textGapHeight: Int = 0
    private var itemHeight: Int = 0
    private var textHeight: Int = 0
    private var previousScrollerY: Int = 0
    private var onValueChangeListener: OnValueChangeListener? = null
    private var onScrollListener: OnScrollListener? = null
    private var adapter: NumberPickerAdapter? = null
    private var fadingEdgeEnabled = true

    private var mScrollState = OnScrollListener.SCROLL_STATE_IDLE

    init {
        val attributesArray = context.obtainStyledAttributes(attrs, R.styleable.NumberPicker, defStyleAttr, 0)

        selectorItemCount = attributesArray.getInt(R.styleable.NumberPicker_np_wheelItemCount, Companion.DEFAULT_ITEM_COUNT) + 2
        wheelMiddleItemIndex = (selectorItemCount - 1) / 2
        selectorVisibleItemCount = selectorItemCount - 2
        wheelVisibleItemMiddleIndex = (selectorVisibleItemCount - 1) / 2
        selectorItemIndices = ArrayList(selectorItemCount)

        minIndex = attributesArray.getInt(R.styleable.NumberPicker_np_min, Integer.MIN_VALUE)
        maxIndex = attributesArray.getInt(R.styleable.NumberPicker_np_max, Integer.MAX_VALUE)
        wrapSelectorWheelPreferred = attributesArray.getBoolean(R.styleable.NumberPicker_np_wrapSelectorWheel, false)


        overScroller = OverScroller(context, DecelerateInterpolator(2.5f))
        val configuration = ViewConfiguration.get(context)
        touchSlop = configuration.scaledTouchSlop
        maximumVelocity = configuration.scaledMaximumFlingVelocity / SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT
        minimumVelocity = configuration.scaledMinimumFlingVelocity

        selectedTextColor = attributesArray.getColor(
            R.styleable.NumberPicker_np_selectedTextColor
            , ContextCompat.getColor(context, R.color.black)
        );
        unSelectedTextColor = attributesArray.getColor(
            R.styleable.NumberPicker_np_textColor
            , ContextCompat.getColor(context, R.color.color_subtitle_setting_profile)
        );
        textSize = attributesArray.getDimensionPixelSize(R.styleable.NumberPicker_np_textSize, DEFAULT_TEXT_SIZE);
        val textAlignInt = attributesArray.getInt(R.styleable.NumberPicker_np_align, 1)
        textAlign = when (textAlignInt) {
            0 -> "LEFT"
            1 -> "CENTER"
            2 -> "RIGHT"
            else -> "CENTER"
        }
        fadingEdgeEnabled = attributesArray.getBoolean(R.styleable.NumberPicker_np_fadingEdgeEnabled, true)

        textPaint = Paint()
        textPaint!!.isAntiAlias = true
        textPaint!!.textSize = textSize.toFloat()
        textPaint!!.textAlign = Paint.Align.valueOf(textAlign)
        textPaint!!.style = Paint.Style.FILL_AND_STROKE
        textPaint!!.isFakeBoldText = true

        attributesArray.recycle()

        initializeSelectorWheelIndices()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (changed) {
            initializeSelectorWheel()
            initializeFadingEdges()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var lp: ViewGroup.LayoutParams? = layoutParams
        if (lp == null)
            lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        var width = calculateSize(suggestedMinimumWidth, lp.width, widthMeasureSpec)
        var height = calculateSize(suggestedMinimumHeight, lp.height, heightMeasureSpec)

        width += paddingLeft + paddingRight
        height += paddingTop + paddingBottom


        setMeasuredDimension(width, height)
    }

    override fun getSuggestedMinimumWidth(): Int {
        var suggested = super.getSuggestedMinimumHeight()
        if (selectorVisibleItemCount > 0) {
            suggested = Math.max(suggested, computeMaximumWidth())
        }
        return suggested
    }

    override fun getSuggestedMinimumHeight(): Int {
        var suggested = super.getSuggestedMinimumWidth()
        if (selectorVisibleItemCount > 0) {
            val fontMetricsInt = textPaint!!.fontMetricsInt
            val height = fontMetricsInt.descent - fontMetricsInt.ascent
            suggested = Math.max(suggested, height * selectorVisibleItemCount)
        }
        return suggested
    }

    private fun computeMaximumWidth(): Int {
        textPaint!!.textSize = textSize * 1.3f
        if (adapter != null) {
            return if (!adapter!!.getTextWithMaximumLength().isEmpty()) {
                val suggestedWith = textPaint!!.measureText(adapter!!.getTextWithMaximumLength()).toInt()
                textPaint!!.textSize = textSize * 1.0f
                suggestedWith
            } else {
                val suggestedWith = textPaint!!.measureText("0000").toInt()
                textPaint!!.textSize = textSize * 1.0f
                suggestedWith
            }
        }
        val widthForMinIndex = textPaint!!.measureText(minIndex.toString()).toInt()
        val widthForMaxIndex = textPaint!!.measureText(maxIndex.toString()).toInt()
        textPaint!!.textSize = textSize * 1.0f
        return if (widthForMinIndex > widthForMaxIndex)
            widthForMinIndex
        else
            widthForMaxIndex
    }

    private fun calculateSize(suggestedSize: Int, paramSize: Int, measureSpec: Int): Int {
        var result = 0
        val size = View.MeasureSpec.getSize(measureSpec)
        val mode = View.MeasureSpec.getMode(measureSpec)

        when (View.MeasureSpec.getMode(mode)) {
            View.MeasureSpec.AT_MOST ->

                if (paramSize == ViewGroup.LayoutParams.WRAP_CONTENT)
                    result = Math.min(suggestedSize, size)
                else if (paramSize == ViewGroup.LayoutParams.MATCH_PARENT)
                    result = size
                else {
                    result = Math.min(paramSize, size)
                }
            View.MeasureSpec.EXACTLY -> result = size
            View.MeasureSpec.UNSPECIFIED ->

                result = if (paramSize == ViewGroup.LayoutParams.WRAP_CONTENT || paramSize == ViewGroup.LayoutParams
                        .MATCH_PARENT
                )
                    suggestedSize
                else {
                    paramSize
                }
        }

        return result
    }

    private fun initializeSelectorWheel() {
        itemHeight = getItemHeight()
        textHeight = computeTextHeight()
        textGapHeight = getGapHeight()

        val visibleMiddleItemPos = itemHeight * wheelVisibleItemMiddleIndex + (itemHeight + textHeight) / 2
        initialFirstItemOffset = visibleMiddleItemPos - itemHeight * wheelMiddleItemIndex
        currentFirstItemOffset = initialFirstItemOffset
    }

    private fun initializeFadingEdges() {
        isVerticalFadingEdgeEnabled = fadingEdgeEnabled
        if (fadingEdgeEnabled)
            setFadingEdgeLength((bottom - top - textSize) / 2)
    }

    private fun initializeSelectorWheelIndices() {
        selectorItemIndices.clear()
        for (i in 0 until selectorItemCount) {
            var selectorIndex = i - wheelMiddleItemIndex
            if (wrapSelectorWheelPreferred) {
                selectorIndex = getWrappedSelectorIndex(selectorIndex)
            }
            selectorItemIndices.add(selectorIndex)
        }
    }

    override fun getBottomFadingEdgeStrength(): Float {
        return TOP_AND_BOTTOM_FADING_EDGE_STRENGTH
    }

    override fun getTopFadingEdgeStrength(): Float {
        return TOP_AND_BOTTOM_FADING_EDGE_STRENGTH
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawVertical(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        onTouchEventVertical(event)
        return true
    }

    private fun onTouchEventVertical(event: MotionEvent) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain()
        }

        velocityTracker?.addMovement(event)

        val action: Int = event.actionMasked
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                if (!overScroller!!.isFinished)
                    overScroller!!.forceFinished(true)


                lastY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                var deltaY = event.y - lastY
                if (!isDragging && Math.abs(deltaY) > touchSlop) {
                    parent?.requestDisallowInterceptTouchEvent(true)

                    if (deltaY > 0) {
                        deltaY -= touchSlop
                    } else {
                        deltaY += touchSlop
                    }
                    onScrollStateChange(OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    isDragging = true
                }

                if (isDragging) {
                    scrollBy(0, deltaY.toInt())
                    invalidate()
                    lastY = event.y
                }
            }
            MotionEvent.ACTION_UP -> {
                if (isDragging) {
                    isDragging = false;
                    parent?.requestDisallowInterceptTouchEvent(false)

                    velocityTracker?.computeCurrentVelocity(1000, maximumVelocity.toFloat())
                    val velocity = velocityTracker?.yVelocity?.toInt()

                    if (Math.abs(velocity!!) > minimumVelocity) {
                        previousScrollerY = 0
                        overScroller?.fling(
                            scrollX, scrollY, 0, velocity, 0, 0, Integer.MIN_VALUE,
                            Integer.MAX_VALUE, 0, (getItemHeight() * 0.7).toInt()
                        )
                        invalidateOnAnimation()
                        onScrollStateChange(OnScrollListener.SCROLL_STATE_FLING)
                    }
                    recyclerVelocityTracker()
                } else {
                    //click event
                    val y = event.y.toInt()
                    handlerClickVertical(y)
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                if (isDragging) {
                    isDragging = false
                }
                recyclerVelocityTracker()
            }
        }
    }

    private fun handlerClickVertical(y: Int) {
        val selectorIndexOffset = y / itemHeight - wheelVisibleItemMiddleIndex
        changeValueBySteps(selectorIndexOffset)
    }

    override fun scrollBy(x: Int, y: Int) {
        if (y == 0)
            return

        val gap = textGapHeight

        if (!wrapSelectorWheelPreferred && y > 0 && selectorItemIndices[wheelMiddleItemIndex] <= minIndex) {
            if (currentFirstItemOffset + y - initialFirstItemOffset < gap / 2)
                currentFirstItemOffset += y
            else {
                currentFirstItemOffset = initialFirstItemOffset + (gap / 2)
                if (!overScroller!!.isFinished && !isDragging) {
                    overScroller!!.abortAnimation()
                }
            }
            return
        }

        if (!wrapSelectorWheelPreferred && y < 0
            && selectorItemIndices[wheelMiddleItemIndex] >= maxIndex
        ) {

            if (currentFirstItemOffset + y - initialFirstItemOffset > -(gap / 2))
                currentFirstItemOffset += y
            else {
                currentFirstItemOffset = initialFirstItemOffset - (gap / 2)
                if (!overScroller!!.isFinished && !isDragging) {
                    overScroller!!.abortAnimation()
                }
            }
            return
        }

        currentFirstItemOffset += y

        while (currentFirstItemOffset - initialFirstItemOffset < -gap) {
            currentFirstItemOffset += itemHeight
            increaseSelectorsIndex()
            if (!wrapSelectorWheelPreferred && selectorItemIndices[wheelMiddleItemIndex] >= maxIndex) {
                currentFirstItemOffset = initialFirstItemOffset
            }
        }

        while (currentFirstItemOffset - initialFirstItemOffset > gap) {
            currentFirstItemOffset -= itemHeight
            decreaseSelectorsIndex()
            if (!wrapSelectorWheelPreferred && selectorItemIndices[wheelMiddleItemIndex] <= minIndex) {
                currentFirstItemOffset = initialFirstItemOffset
            }
        }
        onSelectionChanged(selectorItemIndices[wheelMiddleItemIndex], true)
    }

    override fun computeScroll() {
        super.computeScroll()
        if (overScroller!!.computeScrollOffset()) {
            val x = overScroller!!.currX
            val y = overScroller!!.currY


            if (previousScrollerY == 0) {
                previousScrollerY = overScroller!!.startY
            }
            scrollBy(x, y - previousScrollerY)
            previousScrollerY = y
            invalidate()
        } else {
            if (!isDragging)
            //align item
                adjustItemVertical()
        }
    }

    private fun adjustItemVertical() {
        previousScrollerY = 0
        var deltaY = initialFirstItemOffset - currentFirstItemOffset

        if (Math.abs(deltaY) > itemHeight / 2) {
            deltaY += if (deltaY > 0)
                -itemHeight
            else
                itemHeight
        }

        if (deltaY != 0) {
            overScroller!!.startScroll(scrollX, scrollY, 0, deltaY, 800)
            invalidateOnAnimation()
        }

        onScrollStateChange(OnScrollListener.SCROLL_STATE_IDLE)
    }

    private fun recyclerVelocityTracker() {
        velocityTracker?.recycle()
        velocityTracker = null
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
    }

    private fun onScrollStateChange(scrollState: Int) {
        if (mScrollState == scrollState) {
            return
        }
        mScrollState = scrollState
        onScrollListener?.onScrollStateChange(scrollState)
    }

    private fun getItemHeight(): Int {
        return height / (selectorItemCount - 2)
    }

    private fun getGapHeight(): Int {
        return getItemHeight() - computeTextHeight()
    }

    private fun computeTextHeight(): Int {
        val metricsInt = textPaint!!.fontMetricsInt
        return Math.abs(metricsInt.bottom + metricsInt.top)
    }

    private fun invalidateOnAnimation() {
        if (Build.VERSION.SDK_INT >= 16)
            postInvalidateOnAnimation()
        else
            invalidate()
    }

    private fun drawVertical(canvas: Canvas) {
        if (selectorItemIndices.size == 0)
            return
        val itemHeight = getItemHeight()

        val x = when (textPaint!!.textAlign) {
            Paint.Align.LEFT -> paddingLeft.toFloat()
            Paint.Align.CENTER -> ((right - left) / 2).toFloat()
            Paint.Align.RIGHT -> (right - left).toFloat() - paddingRight.toFloat()
            else -> ((right - left) / 2).toFloat()
        }

        var y = currentFirstItemOffset.toFloat()


        var i = 0

        val topIndexDiffToMid = wheelVisibleItemMiddleIndex;
        val bottomIndexDiffToMid = selectorVisibleItemCount - wheelVisibleItemMiddleIndex - 1
        val maxIndexDiffToMid = Math.max(topIndexDiffToMid, bottomIndexDiffToMid)

        while (i < selectorItemIndices.size) {
            var scale = 1f

            val offsetToMiddle = Math.abs(y - (initialFirstItemOffset + wheelMiddleItemIndex * itemHeight).toFloat())

            if (maxIndexDiffToMid != 0)
                scale = 0.3f * (itemHeight * maxIndexDiffToMid - offsetToMiddle) / (itemHeight * maxIndexDiffToMid) + 1

            if (offsetToMiddle < this.itemHeight / 2) {
                textPaint!!.color = selectedTextColor
            } else {
                textPaint!!.color = unSelectedTextColor
            }
            canvas.save()
            canvas.scale(scale, scale, x, y)
            canvas.drawText(getValue(selectorItemIndices[i]), x, y, textPaint!!)
            canvas.restore()

            y += itemHeight
            i++
        }
    }

    private fun getPosition(value: String): Int = when {
        adapter != null -> adapter!!.getPosition(value)
        else -> try {
            val position = value.toInt()
            validatePosition(position)
        } catch (e: NumberFormatException) {
            0
        }
    }

    private fun increaseSelectorsIndex() {
        for (i in 0 until (selectorItemIndices.size - 1)) {
            selectorItemIndices[i] = selectorItemIndices[i + 1]
        }
        var nextScrollSelectorIndex = selectorItemIndices[selectorItemIndices.size - 2] + 1
        if (wrapSelectorWheelPreferred && nextScrollSelectorIndex > maxIndex) {
            nextScrollSelectorIndex = minIndex
        }
        selectorItemIndices[selectorItemIndices.size - 1] = nextScrollSelectorIndex
    }

    private fun decreaseSelectorsIndex() {
        for (i in selectorItemIndices.size - 1 downTo 1) {
            selectorItemIndices[i] = selectorItemIndices[i - 1]
        }
        var nextScrollSelectorIndex = selectorItemIndices[1] - 1
        if (wrapSelectorWheelPreferred && nextScrollSelectorIndex < minIndex) {
            nextScrollSelectorIndex = maxIndex
        }
        selectorItemIndices[0] = nextScrollSelectorIndex
    }

    private fun changeValueBySteps(steps: Int) {
        previousScrollerY = 0
        overScroller!!.startScroll(0, 0, 0, -itemHeight * steps, Companion.SNAP_SCROLL_DURATION)
        invalidate()
    }

    private fun onSelectionChanged(current: Int, notifyChange: Boolean) {
        val previous = curSelectedItemIndex
        curSelectedItemIndex = current
        if (notifyChange && previous != current) {
            notifyChange(previous, current)
        }
    }

    private fun getWrappedSelectorIndex(selectorIndex: Int): Int {
        if (selectorIndex > maxIndex) {
            return minIndex + (selectorIndex - maxIndex) % (maxIndex - minIndex + 1) - 1
        } else if (selectorIndex < minIndex) {
            return maxIndex - (minIndex - selectorIndex) % (maxIndex - minIndex + 1) + 1
        }
        return selectorIndex
    }

    private fun notifyChange(previous: Int, current: Int) {
        onValueChangeListener?.onValueChange(getValue(previous), getValue(current))
    }

    private fun validatePosition(position: Int): Int {
        return if (!wrapSelectorWheelPreferred) {
            when {
                position > maxIndex -> maxIndex
                position < minIndex -> minIndex
                else -> position
            }
        } else {
            getWrappedSelectorIndex(position)
        }
    }

    fun scrollTo(position: Int) {
        curSelectedItemIndex = position
        selectorItemIndices.clear()
        for (i in 0 until selectorItemCount) {
            var selectorIndex = curSelectedItemIndex + (i - wheelMiddleItemIndex)
            if (wrapSelectorWheelPreferred) {
                selectorIndex = getWrappedSelectorIndex(selectorIndex)
            }
            selectorItemIndices.add(selectorIndex)
        }
    }

    fun setOnValueChangedListener(onValueChangeListener: OnValueChangeListener) {
        this.onValueChangeListener = onValueChangeListener
    }

    fun setOnScrollListener(onScrollListener: OnScrollListener) {
        this.onScrollListener = onScrollListener
    }

    fun smoothScrollTo(position: Int) {
        val realPosition = validatePosition(position)
        changeValueBySteps(realPosition - curSelectedItemIndex)
    }

    fun smoothScrollToValue(value: String) {
        smoothScrollTo(getPosition(value))
    }

    fun scrollToValue(value: String) {
        scrollTo(getPosition(value))
    }

    fun setUnselectedTextColor(resourceId: Int) {
        unSelectedTextColor = resourceId
    }

    fun setAdapter(adapter: NumberPickerAdapter?, indexRangeBasedOnAdapterSize: Boolean = true) {
        this.adapter = adapter
        if (this.adapter == null) {
            invalidate()
            return
        }

        if (adapter!!.getSize() != -1 && indexRangeBasedOnAdapterSize) {
            maxIndex = adapter.getSize() - 1
            minIndex = 0
        }
        invalidate()

        this.adapter?.picker = this
    }

    fun setWrapSelectorWheel(wrap: Boolean) {
        wrapSelectorWheelPreferred = wrap
        requestLayout()
    }

    fun getWrapSelectorWheel(): Boolean {
        return wrapSelectorWheelPreferred
    }

    fun setWheelItemCount(count: Int) {
        selectorItemCount = count + 2
        wheelMiddleItemIndex = (selectorItemCount - 1) / 2
        selectorVisibleItemCount = selectorItemCount - 2
        wheelVisibleItemMiddleIndex = (selectorVisibleItemCount - 1) / 2
        selectorItemIndices = ArrayList(selectorItemCount)
        reset()
        requestLayout()
    }

    fun setSelectedTextColor(colorId: Int) {
        selectedTextColor = ContextCompat.getColor(context, colorId);
        requestLayout()
    }

    fun getValue(position: Int): String = when {
        adapter != null -> adapter!!.getValue(position)
        else -> if (!wrapSelectorWheelPreferred) {
            when {
                position > maxIndex -> ""
                position < minIndex -> ""
                else -> position.toString()
            }
        } else {
            getWrappedSelectorIndex(position).toString()
        }
    }

    fun setValue(value: String) {
        scrollToValue(value)
    }

    fun setMaxValue(max: Int) {
        maxIndex = max
    }

    fun getMaxValue(): String {
        return if (adapter != null) {
            adapter!!.getValue(maxIndex)
        } else {
            maxIndex.toString()
        }
    }

    fun setMinValue(min: Int) {
        minIndex = min
    }

    fun getMinValue(): String {
        return if (adapter != null) {
            adapter!!.getValue(minIndex)
        } else {
            minIndex.toString()
        }
    }

    fun reset() {
        initializeSelectorWheelIndices()
        initializeSelectorWheel()
        requestLayout()
    }

    fun getCurrentItem(): String {
        return getValue(curSelectedItemIndex)
    }

    companion object {
        private const val TOP_AND_BOTTOM_FADING_EDGE_STRENGTH = 0.9f
        private const val SNAP_SCROLL_DURATION = 300
        private const val SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT = 4
        private const val DEFAULT_ITEM_COUNT = 3
        private const val DEFAULT_TEXT_SIZE = 80
    }
}