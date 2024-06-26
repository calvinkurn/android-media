package com.tokopedia.hotel.search_map.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.util.CurrencyEnum
import com.tokopedia.hotel.common.util.CurrencyTextWatcher
import com.tokopedia.hotel.databinding.LayoutHotelFilterPriceRangeSliderBinding
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.RangeSliderUnify
import kotlin.math.ceil
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * @author by jessica on 31/03/20
 */

class HotelFilterPriceRangeSlider @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val binding = LayoutHotelFilterPriceRangeSliderBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    var maxBound = 0
    private lateinit var minCurrencyTextWatcher: CurrencyTextWatcher
    private lateinit var maxCurrencyTextWatcher: CurrencyTextWatcher

    var onValueChangedListener: OnValueChangedListener? = null

    fun initView(selectedMinPrice: Int, selectedMaxPrice: Int, maxBound: Int) {
        with(binding) {
            this@HotelFilterPriceRangeSlider.maxBound = maxBound

            if (::minCurrencyTextWatcher.isInitialized) {
                minValue.removeTextChangedListener(minCurrencyTextWatcher)
            }
            minCurrencyTextWatcher = CurrencyTextWatcher(minValue, CurrencyEnum.RP)
            minValue.addTextChangedListener(minCurrencyTextWatcher)
            minValue.setText(selectedMinPrice.toString())

            if (::maxCurrencyTextWatcher.isInitialized) {
                maxValue.removeTextChangedListener(maxCurrencyTextWatcher)
            }
            maxCurrencyTextWatcher = CurrencyTextWatcher(maxValue, CurrencyEnum.RP)
            maxValue.addTextChangedListener(maxCurrencyTextWatcher)

            if (selectedMaxPrice == 0 || selectedMaxPrice == maxBound) maxCurrencyTextWatcher.stringFormat = resources.getString(R.string.hotel_search_filter_max_string_format_with_plus)
            if (selectedMaxPrice != 0) maxValue.setText(selectedMaxPrice.toString())
            else maxValue.setText(maxBound.toString())

            minLabel.text = resources.getString(R.string.hotel_search_filter_price_min_label)
            maxLabel.text = resources.getString(R.string.hotel_search_filter_price_max_label)

            seekbarBackground.activeRailColor = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_GN300)
            seekbarBackground.activeBackgroundRailColor = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN50)
            seekbarBackground.activeKnobColor = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_GN500)

            seekbarBackground.updateEndValue(getPositionFromMaxValue(maxBound))
            seekbarBackground.setInitialValue(getPositionFromMinValue(selectedMinPrice), getPositionFromMaxValue(selectedMaxPrice))

            seekbarBackground.onSliderMoveListener = object : RangeSliderUnify.OnSliderMoveListener {
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

                    minValue.setText(startValue.toString())
                    if (endValue >= maxBound) maxCurrencyTextWatcher.stringFormat = resources.getString(R.string.hotel_search_filter_max_string_format_with_plus)
                    else maxCurrencyTextWatcher.stringFormat = resources.getString(R.string.hotel_search_filter_max_string_format)
                    maxValue.setText(endValue.toString())
                }
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
