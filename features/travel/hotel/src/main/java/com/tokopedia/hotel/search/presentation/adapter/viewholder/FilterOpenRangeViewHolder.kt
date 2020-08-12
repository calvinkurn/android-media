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

class FilterOpenRangeViewHolder(view: View): HotelSearchResultFilterV2Adapter.FilterBaseViewHolder(view) {

    override var selectedOption: ParamFilterV2 = ParamFilterV2()

    override fun bind(filter: FilterV2) {
        selectedOption.name = filter.name
        selectedOption.values = filter.optionSelected

        with(itemView) {
            hotel_filter_open_range_title.text = filter.displayName

            hotel_filter_open_range_slider.initView((filter.optionSelected.firstOrNull() ?: "0").toIntOrZero(),
                    (filter.optionSelected.lastOrNull() ?: "0").toIntOrZero(), filter.options.lastOrNull().toIntOrZero())
            hotel_filter_open_range_slider.onValueChangedListener = object: HotelFilterPriceRangeSlider.OnValueChangedListener{
                override fun onValueChanged(startValue: Int, endValue: Int) {
                    selectedOption.values = listOf(startValue.toString(), endValue.toString())
                }
            }
        }
    }

    override fun resetSelection() {

    }

    companion object {
        val LAYOUT = R.layout.layout_hotel_filter_open_range
    }
}