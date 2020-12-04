package com.tokopedia.hotel.search.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.util.CurrencyEnum
import com.tokopedia.hotel.common.util.CurrencyTextWatcher
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.RangeSliderUnify
import kotlinx.android.synthetic.main.layout_hotel_filter_price_range_slider.view.*
import kotlin.math.ceil

/**
 * @author by jessica on 31/03/20
 */

class HotelFilterPriceRangeSlider @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    var maxBound = 0
    private lateinit var minCurrencyTextWatcher: CurrencyTextWatcher
    private lateinit var maxCurrencyTextWatcher: CurrencyTextWatcher

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

        if (selectedMaxPrice == 0 || selectedMaxPrice == maxBound) maxCurrencyTextWatcher.stringFormat = resources.getString(R.string.hotel_search_filter_max_string_format_with_plus)
        if (selectedMaxPrice != 0) max_value.setText(selectedMaxPrice.toString())
        else max_value.setText(maxBound.toString())

        min_label.text = resources.getString(R.string.hotel_search_filter_price_min_label)
        max_label.text = resources.getString(R.string.hotel_search_filter_price_max_label)

        seekbar_background.activeRailColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G300)
        seekbar_background.backgroundRailColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N75)
        seekbar_background.knobColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)

        seekbar_background.updateEndValue(getPositionFromMaxValue(maxBound))
        seekbar_background.setInitialValue(getPositionFromMinValue(selectedMinPrice), getPositionFromMaxValue(selectedMaxPrice))

        seekbar_background.onSliderMoveListener = object : RangeSliderUnify.OnSliderMoveListener {
            override fun onSliderMove(p0: Pair<Int, Int>) {
                val (start, end) = p0
                var endValue = 0
                var startValue = 0

                if (start < ONE_HUNDRED) {
                    startValue = (start * ONE_THOUSAND)
                }
                if (start in ONE_HUNDRED..(STEP_190 + 1)) {
                    startValue = ((start - STEP_100) * TEN_THOUSAND) + ONE_HUNDRED_THOUSAND
                }
                if (start in STEP_190..(STEP_280 + 1)) {
                    startValue = ((start - STEP_190) * ONE_HUNDRED_THOUSAND) + ONE_MILLION
                }
                if (start > STEP_280) {
                    startValue = ((start - STEP_280) * ONE_MILLION) + TEN_MILLION
                }

                if (end < (STEP_99)) {
                    endValue = (ceil(end * (1 - (MIN_BOUND / ONE_HUNDRED_THOUSAND.toFloat()))).toInt() * ONE_THOUSAND) + MIN_BOUND
                }
                if (end in (STEP_99)..STEP_190) {
                    endValue = ((end - STEP_99) * TEN_THOUSAND) + ONE_HUNDRED_THOUSAND
                }
                if (end in (STEP_189)..STEP_280) {
                    endValue = ((end - STEP_189) * ONE_HUNDRED_THOUSAND) + ONE_MILLION
                }
                if (end >= STEP_280) {
                    endValue = ((end - STEP_279) * ONE_MILLION) + TEN_MILLION
                }

                onValueChangedListener?.onValueChanged(startValue, endValue)

                min_value.setText(startValue.toString())
                if (endValue >= maxBound) maxCurrencyTextWatcher.stringFormat = resources.getString(R.string.hotel_search_filter_max_string_format_with_plus)
                else maxCurrencyTextWatcher.stringFormat = resources.getString(R.string.hotel_search_filter_max_string_format)
                max_value.setText(endValue.toString())

            }
        }
    }

    private fun getPositionFromMinValue(value: Int): Int {
        if (value >= 0) {
            when {
                value < ONE_HUNDRED_THOUSAND -> return value / ONE_THOUSAND
                value in ONE_HUNDRED_THOUSAND until ONE_MILLION -> return ((value - ONE_HUNDRED_THOUSAND) / TEN_THOUSAND) + STEP_100
                value in ONE_MILLION until TEN_MILLION -> return ((value - ONE_MILLION) / ONE_HUNDRED_THOUSAND) + STEP_190
                value >= TEN_MILLION -> return ((value - TEN_MILLION) / ONE_MILLION) + STEP_280
            }
        }
        return getPositionFromMinValue(0)
    }

    private fun getPositionFromMaxValue(value: Int): Int {
        if (value > 0) {
            when {
                value < ONE_HUNDRED_THOUSAND -> return ceil((value - MIN_BOUND) / ONE_THOUSAND / (1 - (MIN_BOUND / ONE_HUNDRED_THOUSAND.toFloat()))).toInt()
                value in ONE_HUNDRED_THOUSAND until ONE_MILLION -> return ((value - ONE_HUNDRED_THOUSAND) / TEN_THOUSAND) + STEP_99
                value in ONE_MILLION until TEN_MILLION -> return ((value - ONE_MILLION) / ONE_HUNDRED_THOUSAND) + STEP_189
                value >= TEN_MILLION -> return ((value - TEN_MILLION) / ONE_MILLION) + STEP_279
            }
        }
        return getPositionFromMaxValue(maxBound)
    }

    interface OnValueChangedListener {
        fun onValueChanged(startValue: Int, endValue: Int)
    }

    companion object {
        const val MIN_BOUND = 20000

        const val ONE_HUNDRED = 100
        const val ONE_THOUSAND = 1000
        const val TEN_THOUSAND  = 10000
        const val ONE_HUNDRED_THOUSAND  = 100000
        const val ONE_MILLION = 1000000
        const val TEN_MILLION = 10000000

        const val STEP_100 = 100
        const val STEP_190 = 190
        const val STEP_280 = 280

        const val STEP_99 = 99
        const val STEP_189 = 189
        const val STEP_279 = 279
    }
}