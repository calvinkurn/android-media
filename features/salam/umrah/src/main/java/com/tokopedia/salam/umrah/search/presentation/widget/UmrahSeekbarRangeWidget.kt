package com.tokopedia.salam.umrah.search.presentation.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.PriceRangeLimit
import com.tokopedia.salam.umrah.search.presentation.adapter.UmrahSeekbarRangeWidgetAdapter
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_umrah_seekbar_range.view.*

/**
 * @author by M on 18/10/19
 */

class UmrahSeekbarRangeWidget @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private var isMinThumbDragging = false
    private var isMaxThumbDragging = false
    private var seekBarThumbSize = 0

    private var seekBarThumbMaxPosition = 0
    private var seekBarThumbMinPosition = 0
    private var minValue = 0
    private var maxValue = 0
    var rangeValue = 0
    private var separatorSize = 0f
    private var selectedFilter = mutableListOf<Int>()
    private var spanWidth: Float = 0.0f
    private var minThumbPosition = 0
    private var maxThumbPosition = 0

    private val filterSeekbarRangeWidgetAdapter: UmrahSeekbarRangeWidgetAdapter by lazy { UmrahSeekbarRangeWidgetAdapter() }
    private var seekbarNumbers = mutableListOf<Int>()

    init {
        val view = View.inflate(context, R.layout.widget_umrah_seekbar_range, this)
        seekBarThumbSize = resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_30)
        if (seekBarWidth == 0F) {
            view.afterMeasured {
                seekBarLeftOffset = this@UmrahSeekbarRangeWidget.dbs_background.x
                seekBarWidth = this@UmrahSeekbarRangeWidget.dbs_background.width.toFloat()
                seekBarRange = seekBarWidth - (2 * this@UmrahSeekbarRangeWidget.seekBarThumbSize).toFloat()
                getTypographyView()
            }
        }
        discretePositions.clear()
    }

    private inline fun View.afterMeasured(crossinline f: View.() -> Unit) {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (measuredWidth > 0 && measuredHeight > 0) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    f()
                }
            }
        })
    }

    fun getMinValue() = seekBarThumbMinPosition + minValue

    fun getMaxValue() = seekBarThumbMaxPosition + minValue

    fun setRange(range: PriceRangeLimit) {
        minValue = range.minimum
        maxValue = range.maximum
        rangeValue = maxValue - minValue
        for (i in minValue..maxValue) {
            seekbarNumbers.add(i)
            selectedFilter.add(i)
        }
        initSeekbarNumbersAdapter()
    }

    private fun initSeekbarNumbersAdapter() {
        filterSeekbarRangeWidgetAdapter.items = seekbarNumbers
        val spanningLinearLayoutManager = SpanningLinearLayoutManager(context, RecyclerView.HORIZONTAL, false, resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16))
        rv_umrah_seekbar_range.layoutManager = spanningLinearLayoutManager
    }

    fun setData(minThumbPosition: Int, maxThumbPosition: Int) {
        this@UmrahSeekbarRangeWidget.minThumbPosition = minThumbPosition
        this@UmrahSeekbarRangeWidget.maxThumbPosition = maxThumbPosition
        getTypographyView()
    }

    private fun getTypographyView() {
        val itemCount = seekbarNumbers.size
        spanWidth = seekBarWidth / itemCount.toFloat()
        filterSeekbarRangeWidgetAdapter.setSpanWidth(spanWidth)
        rv_umrah_seekbar_range.adapter = filterSeekbarRangeWidgetAdapter
        filterSeekbarRangeWidgetAdapter.updateSelectedItems(selectedFilter.map { it }.toSet())
        getItemNumberWidth()
        updateSeekbar()
    }

    private fun updateSeekbar() {
        val itemCount = seekbarNumbers.size
        val halfSpanWidth = (spanWidth / 2)
        val quarterSpanWidth = (halfSpanWidth / 3.8f)
        if (discretePositions.isEmpty() && seekBarWidth != 0f && itemNumberWidth != 0) {
            val initMargin = halfSpanWidth - itemNumberWidth.toFloat() / 2 - quarterSpanWidth
            discretePositions.clear()
            for (i in 0 until itemCount) {
                discretePositions.add((i * spanWidth) + initMargin - (seekBarThumbSize.toFloat() / 4))
            }
            separatorSize = halfSpanWidth
            refreshThumbPosition(minThumbPosition, maxThumbPosition)
        }
    }

    private fun getItemNumberWidth() {
        filterSeekbarRangeWidgetAdapter.listener = object : UmrahSeekbarRangeWidgetAdapter.SeekbarListener {
            override fun onItemWidthMeasured(itemWidth: Int) {
                itemNumberWidth = itemWidth
                updateSeekbar()
            }
        }
    }

    private fun refreshThumbPosition(minThumbPosition: Int, maxThumbPosition: Int) {
        val realMinThumbPosition = minThumbPosition - minValue
        val realMaxThumbPosition = maxThumbPosition - minValue
        thumb_min.x = discretePositions[realMinThumbPosition]
        thumb_max.x = discretePositions[realMaxThumbPosition]

        seekBarThumbMinPosition = discretePositions.indexOf(thumb_min.x)
        seekBarThumbMaxPosition = discretePositions.indexOf(thumb_max.x)
        refreshSeekbarBackground()
    }

    private fun refreshSeekbarBackground() {
        dbs_background.setFirstPointPercentage((thumb_min.x - seekBarLeftOffset) / seekBarWidth)
        dbs_background.setSecondPointPercentage((thumb_max.x - seekBarLeftOffset) / seekBarWidth)
        dbs_background.invalidate()
    }

    private fun isPointInsideView(x: Float, y: Float, view: View): Boolean {
        val viewArea = Rect()
        view.getGlobalVisibleRect(viewArea)
        return viewArea.contains(x.toInt(), y.toInt())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            0 -> {
                if (isPointInsideView(event.rawX, event.rawY, thumb_min)) {
                    isMinThumbDragging = true
                    isMaxThumbDragging = false
                    if (Build.VERSION.SDK_INT >= 21) {
                        thumb_min.elevation = 0.0f
                    }
                } else if (isPointInsideView(event.rawX, event.rawY, thumb_max)) {
                    isMaxThumbDragging = true
                    isMinThumbDragging = false
                    if (Build.VERSION.SDK_INT >= 21) {
                        thumb_max.elevation = 0.0f
                    }
                }
            }
            1 -> {
                isMinThumbDragging = false
                isMaxThumbDragging = false
                if (Build.VERSION.SDK_INT >= 21) {
                    thumb_max.elevation = 8.0f
                    thumb_min.elevation = 8.0f
                }
            }
            2 -> {
                val targetX = event.rawX - seekBarThumbSize.toFloat()
                val position: Float
                when {
                    isMinThumbDragging -> {
                        position = normalizeMinThumbPosition(targetX)
                        thumb_min.x = position
                        seekBarThumbMinPosition = discretePositions.indexOf(position)
                        refreshSeekbarBackground()
                        refreshTypographyDay()
                        requestDisallowInterceptTouchEvent(true)
                    }
                    isMaxThumbDragging -> {
                        position = normalizeMaxThumbPosition(targetX)
                        thumb_max.x = position
                        seekBarThumbMaxPosition = discretePositions.indexOf(position)
                        refreshSeekbarBackground()
                        refreshTypographyDay()
                        requestDisallowInterceptTouchEvent(true)
                    }
                    else -> requestDisallowInterceptTouchEvent(false)
                }
            }
        }
        return true
    }

    private fun refreshTypographyDay() {
        selectedFilter.clear()
        for (i in minValue..maxValue) {
            if (i >= seekBarThumbMinPosition + minValue && i <= seekBarThumbMaxPosition + minValue) {
                selectedFilter.add(i)
            }
        }
        filterSeekbarRangeWidgetAdapter.updateSelectedItems(selectedFilter.map { it }.toSet())
    }

    private fun normalizeMinThumbPosition(x: Float): Float {
        val zeroPosition = discretePositions[0]
        val rightThumb = discretePositions[seekBarThumbMaxPosition - 1]
        var xInDiscrete = -1f
        for (index in discretePositions.size - 2 downTo 0) {
            if (x < discretePositions[index] + separatorSize / 2) xInDiscrete = discretePositions[index]
        }
        if (xInDiscrete > rightThumb) xInDiscrete = rightThumb
        return if (x < zeroPosition) {
            zeroPosition
        } else {
            if (x > rightThumb) rightThumb else xInDiscrete
        }
    }

    private fun normalizeMaxThumbPosition(x: Float): Float {
        val fullPosition = discretePositions[discretePositions.size - 1]
        val leftThumb = discretePositions[seekBarThumbMinPosition + 1]
        var xInDiscrete = -1f
        for (index in discretePositions.size - 1 downTo 1) {
            if (x < discretePositions[index] + separatorSize / 2) xInDiscrete = discretePositions[index]
        }
        if (xInDiscrete < leftThumb) xInDiscrete = leftThumb
        return if (x > fullPosition) {
            fullPosition
        } else {
            if (x < leftThumb) return leftThumb
            else xInDiscrete
        }
    }


    companion object {
        private var seekBarLeftOffset = 0F
        private var seekBarRange = 0F
        private var seekBarWidth = 0F
        private var itemNumberWidth = 0
        private var discretePositions = arrayListOf<Float>()
    }
}