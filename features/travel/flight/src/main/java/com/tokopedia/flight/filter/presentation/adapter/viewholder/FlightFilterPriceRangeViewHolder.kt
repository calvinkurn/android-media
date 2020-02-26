package com.tokopedia.flight.filter.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.common.util.FlightCurrencyFormatUtil
import com.tokopedia.flight.filter.presentation.FlightFilterSortListener
import com.tokopedia.flight.filter.presentation.model.PriceRangeModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.RangeSliderUnify
import kotlinx.android.synthetic.main.item_flight_filter_price_range.view.*

/**
 * @author by furqan on 20/02/2020
 */
class FlightFilterPriceRangeViewHolder(val view: View,
                                       private val listener: FlightFilterSortListener)
    : AbstractViewHolder<PriceRangeModel>(view) {

    private var startValue: Int = 0
    private var endValue: Int = 0
    private var isExpanded: Boolean = true

    override fun bind(element: PriceRangeModel) {
        with(view) {
            startValue = element.initialStartValue
            endValue = element.initialEndValue

            etFlightLowestPrice.setText(FlightCurrencyFormatUtil.convertToIdrPriceWithoutSymbol(element.selectedStartValue))
            etFlightHighestPrice.setText(FlightCurrencyFormatUtil.convertToIdrPriceWithoutSymbol(element.selectedEndValue))
            rsuFlightFilterPrice.updateStartValue(element.initialStartValue)
            rsuFlightFilterPrice.updateEndValue(element.initialEndValue)
            rsuFlightFilterPrice.setInitialValue(element.selectedStartValue, element.selectedEndValue)

            rsuFlightFilterPrice.onSliderMoveListener = object : RangeSliderUnify.OnSliderMoveListener {
                override fun onSliderMove(p0: Pair<Int, Int>) {
                    etFlightLowestPrice.setText(FlightCurrencyFormatUtil.convertToIdrPriceWithoutSymbol(p0.first))
                    etFlightHighestPrice.setText(FlightCurrencyFormatUtil.convertToIdrPriceWithoutSymbol(p0.second))
                    listener.onPriceRangeChanged(p0.first, p0.second)
                }
            }

            icFlightFoldableArrow.setOnClickListener {
                if (isExpanded) {
                    containerPriceText.hide()
                    icFlightFoldableArrow.setImageDrawable(ContextCompat.getDrawable(context,
                            com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_down_gray_24))
                    isExpanded = false
                } else {
                    containerPriceText.show()
                    icFlightFoldableArrow.setImageDrawable(ContextCompat.getDrawable(context,
                            com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_up_gray_24))
                    isExpanded = true
                }
            }
        }
    }

    fun resetView() {
        view.rsuFlightFilterPrice.setInitialValue(startValue, endValue)
        view.etFlightLowestPrice.setText(FlightCurrencyFormatUtil.convertToIdrPriceWithoutSymbol(startValue))
        view.etFlightHighestPrice.setText(FlightCurrencyFormatUtil.convertToIdrPriceWithoutSymbol(endValue))
    }

    companion object {
        val LAYOUT = R.layout.item_flight_filter_price_range
    }
}