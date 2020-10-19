package com.tokopedia.hotel.search.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.hotel.R
import com.tokopedia.hotel.search.data.model.FilterV2
import com.tokopedia.hotel.search.data.model.params.ParamFilterV2
import com.tokopedia.hotel.search.presentation.adapter.HotelSearchResultFilterV2Adapter
import com.tokopedia.hotel.search.presentation.widget.HotelFilterPriceRangeSlider
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import kotlinx.android.synthetic.main.layout_hotel_filter_open_range.view.*

/**
 * @author by jessica on 12/08/20
 */

class FilterOpenRangeViewHolder(view: View, val onSelectedFilterChangedListener: OnSelectedFilterChangedListener)
    : HotelSearchResultFilterV2Adapter.FilterBaseViewHolder(view) {

    override var filterName: String = ""
    private var initialMaxPrice = 0

    override fun bind(filter: FilterV2) {
        filterName = filter.name

        if (filter.options.isEmpty()) filter.options = listOf(MIN_PRICE, MAX_PRICE)
        initialMaxPrice = filter.options.lastOrNull().toIntOrZero()

        val selectedFilterOnlyOne = filter.optionSelected.size == 1

        with(itemView) {
            hotel_filter_open_range_title.text = filter.displayName

            if (selectedFilterOnlyOne) {
                hotel_filter_open_range_slider.initView( 0,
                        (filter.optionSelected.getOrNull(0) ?: initialMaxPrice.toString()).toIntOrZero() , initialMaxPrice)
            } else {
                hotel_filter_open_range_slider.initView((filter.optionSelected.firstOrNull() ?: "0").toIntOrZero(),
                        (filter.optionSelected.getOrNull(1) ?: initialMaxPrice.toString()).toIntOrZero() , initialMaxPrice)
            }

            hotel_filter_open_range_slider.onValueChangedListener = object: HotelFilterPriceRangeSlider.OnValueChangedListener{
                override fun onValueChanged(startValue: Int, endValue: Int) {
                    if (startValue.toString() == MIN_PRICE && endValue.toString() == MAX_PRICE) onSelectedFilterChangedListener.onSelectedFilterChanged(filterName)
                    else onSelectedFilterChangedListener.onSelectedFilterChanged(filterName, mutableListOf(startValue.toString(), endValue.toString()))
                }
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_hotel_filter_open_range
        const val MIN_PRICE = "0"
        const val MAX_PRICE = "15000000"
    }
}