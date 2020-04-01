package com.tokopedia.hotel.search.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.design.intdef.CurrencyEnum
import com.tokopedia.design.text.watcher.CurrencyTextWatcher
import com.tokopedia.hotel.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.RangeSliderUnify
import kotlinx.android.synthetic.main.layout_hotel_filter_price_range_slider.view.*

/**
 * @author by jessica on 31/03/20
 */

class HotelFilterPriceRangeSlider @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    var maxBound = 0
    lateinit var minCurrencyTextWatcher: CurrencyTextWatcher
    lateinit var maxCurrencyTextWatcher: CurrencyTextWatcher

    var onValueChangedListener: OnValueChangedListener? = null

    init {
        View.inflate(context, R.layout.layout_hotel_filter_price_range_slider, this)
    }

    fun initView(selectedMinPrice: Int, selectedMaxPrice: Int, maxBound: Int) {
        this.maxBound = maxBound

        if (::minCurrencyTextWatcher.isInitialized) {
            min_value.removeTextChangedListener(minCurrencyTextWatcher)
        }
        minCurrencyTextWatcher = CurrencyTextWatcher(min_value, CurrencyEnum.RP)
        min_value.addTextChangedListener(minCurrencyTextWatcher)
        min_value.setText(selectedMinPrice.toString())

        if (::maxCurrencyTextWatcher.isInitialized) {
            max_value.removeTextChangedListener(maxCurrencyTextWatcher)
        }
        maxCurrencyTextWatcher = CurrencyTextWatcher(max_value, CurrencyEnum.RP)
        max_value.addTextChangedListener(maxCurrencyTextWatcher)

        if (selectedMaxPrice == 0 || selectedMaxPrice == maxBound) maxCurrencyTextWatcher.format = resources.getString(R.string.hotel_search_filter_max_string_format_with_plus)
        if (selectedMaxPrice != 0) max_value.setText(selectedMaxPrice.toString())
        else max_value.setText(maxBound.toString())

        min_label.text = resources.getString(R.string.hotel_search_filter_price_min_label)
        max_label.text = resources.getString(R.string.hotel_search_filter_price_max_label)

        seekbar_background.activeRailColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.light_G300)
        seekbar_background.backgroundRailColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Neutral_N75)
        seekbar_background.knobColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Green_G500)

        seekbar_background.updateEndValue(getPositionFromMaxValue(maxBound))
        seekbar_background.setInitialValue(getPositionFromMinValue(selectedMinPrice), getPositionFromMaxValue(selectedMaxPrice))

        seekbar_background.onSliderMoveListener = object : RangeSliderUnify.OnSliderMoveListener {
            override fun onSliderMove(p0: Pair<Int, Int>) {
                val (start, end) = p0
                var endValue = 0
                var startValue = 0

                if (start >= 0) {
                    if (start < 100) {
                        startValue = (start * 1000)
                    }
                    if (start in 100..191) {
                        startValue = ((start - 100) * 10000) + 100000
                    }
                    if (start in 190..281) {
                        startValue = ((start - 190) * 100000) + 1000000
                    }
                    if (start > 280) {
                        startValue = ((start - 280) * 1000000) + 10000000
                    }
                    onValueChangedListener?.onValueChanged(startValue, -1)
                    min_value.setText(startValue.toString())
                }

                if (end >= 0) {
                    if (end < 99) {
                        endValue = if (end >= 19) ((end + 1) * 1000)
                        else 20000
                    }
                    if (end in 99..190) {
                        endValue = ((end - 99) * 10000) + 100000
                    }
                    if (end in 189..280) {
                        endValue = ((end - 189) * 100000) + 1000000
                    }
                    if (end >= 280) {
                        endValue = ((end - 279) * 1000000) + 10000000
                    }
                    onValueChangedListener?.onValueChanged(-1, endValue)
                    if (endValue >= maxBound) maxCurrencyTextWatcher.format = resources.getString(R.string.hotel_search_filter_max_string_format_with_plus)
                    else maxCurrencyTextWatcher.format = resources.getString(R.string.hotel_search_filter_max_string_format)
                    max_value.setText(endValue.toString())
                }
            }
        }
    }

    private fun getPositionFromMinValue(value: Int): Int {
        if (value >= 0) {
            when {
                value < 100000 -> return value/1000
                value in 100000..999999 -> return ((value - 100000)/10000) + 100
                value in 1000000..9999999 -> return ((value - 1000000)/100000) + 190
                value >= 10000000 -> return ((value - 10000000)/1000000) + 280
            }
        }
        return 0
    }

    private fun getPositionFromMaxValue(value: Int): Int {
        if (value > 0) {
            when {
                value < 100000 -> return value / 1000 - 1
                value in 100000..999999 -> return ((value - 100000) / 10000) + 99
                value in 1000000..9999999 -> return ((value - 1000000) / 100000) + 189
                value >= 10000000 -> return ((value - 10000000) / 1000000) + 279
            }
        }
        return 285
    }

    interface OnValueChangedListener {
        fun onValueChanged(startValue: Int, endValue: Int)
    }
}