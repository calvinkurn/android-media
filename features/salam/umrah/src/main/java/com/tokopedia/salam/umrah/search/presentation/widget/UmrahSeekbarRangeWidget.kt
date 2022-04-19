package com.tokopedia.salam.umrah.search.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.PriceRangeLimit
import com.tokopedia.salam.umrah.search.presentation.adapter.UmrahSeekbarRangeWidgetAdapter
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.RangeSliderUnify
import kotlinx.android.synthetic.main.widget_umrah_seekbar_range.view.*

/**
 * @author by M on 18/10/19
 */

class UmrahSeekbarRangeWidget @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private var seekBarThumbSize = 0

    private var seekBarThumbMaxPosition = 0
    private var seekBarThumbMinPosition = 0
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
        seekBarThumbSize = resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl6)
        view.dbs_background.activeRailColor = resources.getColor(com.tokopedia.unifyprinciples.R.color.Green_G500)
        view.dbs_background.backgroundRailColor = resources.getColor(com.tokopedia.unifyprinciples.R.color.Neutral_N100)
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

    fun getMinValue() = seekBarThumbMinPosition

    fun getMaxValue() = seekBarThumbMaxPosition

    fun setRange(range: PriceRangeLimit) {
        seekBarThumbMinPosition = range.minimum
        seekBarThumbMaxPosition = range.maximum
        dbs_background.updateValue(range.minimum, range.maximum)
        rangeValue = seekBarThumbMaxPosition - seekBarThumbMinPosition
        for (i in seekBarThumbMinPosition..seekBarThumbMaxPosition) {
            seekbarNumbers.add(i)
            selectedFilter.add(i)
        }
        initSeekbarNumbersAdapter()
    }

    private fun initSeekbarNumbersAdapter() {
        filterSeekbarRangeWidgetAdapter.items = seekbarNumbers
        val spanningLinearLayoutManager = SpanningLinearLayoutManager(context, RecyclerView.HORIZONTAL, false, resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4))
        rv_umrah_seekbar_range.layoutManager = spanningLinearLayoutManager
    }

    fun setData(minThumbPosition: Int, maxThumbPosition: Int) {
        dbs_background.updateValue(minThumbPosition, maxThumbPosition)
        seekBarThumbMinPosition = minThumbPosition
        seekBarThumbMaxPosition = maxThumbPosition
        getTypographyView()
        refreshTypographyDay()
        dbs_background.onSliderMoveListener = object: RangeSliderUnify.OnSliderMoveListener {
            override fun onSliderMove(p0: Pair<Int, Int>) {
                val(start, end) = p0
                seekBarThumbMaxPosition = end
                seekBarThumbMinPosition = start
                refreshTypographyDay()
            }
        }
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

    private fun refreshTypographyDay() {
        selectedFilter.clear()
        for (i in seekBarThumbMinPosition..seekBarThumbMaxPosition) {
                selectedFilter.add(i)
        }
        filterSeekbarRangeWidgetAdapter.updateSelectedItems(selectedFilter.map { it }.toSet())
    }

    companion object {
        private var seekBarLeftOffset = 0F
        private var seekBarRange = 0F
        private var seekBarWidth = 0F
        private var itemNumberWidth = 0
        private var discretePositions = arrayListOf<Float>()
    }
}