package com.tokopedia.hotel.search.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.presentation.widget.SpanningLinearLayoutManager
import com.tokopedia.hotel.search.data.model.FilterStarEnum
import com.tokopedia.hotel.search.data.model.FilterV2
import com.tokopedia.hotel.search.data.model.params.ParamFilterV2
import com.tokopedia.hotel.search.presentation.adapter.HotelSearchResultFilterAdapter
import com.tokopedia.hotel.search.presentation.adapter.HotelSearchResultFilterV2Adapter
import kotlinx.android.synthetic.main.layout_hotel_filter_selection.view.*

/**
 * @author by jessica on 12/08/20
 */

class FilterSelectionViewHolder(view: View): HotelSearchResultFilterV2Adapter.FilterBaseViewHolder(view) {

    override var selectedOption = ParamFilterV2()
        get() {
            field.values = adapter.selectedItems.toMutableList()
            return field
        }

    private val adapter: HotelSearchResultFilterAdapter by lazy {
        HotelSearchResultFilterAdapter(HotelSearchResultFilterAdapter.MODE_MULTIPLE)
    }

    override fun bind(filter: FilterV2) {
        selectedOption.name = filter.name
        selectedOption.values = filter.optionSelected.toMutableList()

        with(itemView) {
            hotel_filter_selection_title.text = filter.displayName
            var hotelFilterItems = filter.options.map {
                HotelSearchResultFilterAdapter.HotelFilterItem(it, it, filter.name == SELECTION_STAR_TYPE) }

            if (filter.name == SELECTION_STAR_TYPE) {
                if (hotelFilterItems.isEmpty()) {
                    hotelFilterItems = listOf(FilterStarEnum.STAR_ONE.value,
                            FilterStarEnum.STAR_TWO.value,
                            FilterStarEnum.STAR_THREE.value,
                            FilterStarEnum.STAR_FOUR.value,
                            FilterStarEnum.STAR_FIVE.value).map {
                        HotelSearchResultFilterAdapter.HotelFilterItem(it, it, filter.name == SELECTION_STAR_TYPE)
                    }
                }
                hotel_filter_selection_rv.layoutManager = SpanningLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                        false, resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1))
            } else {
                hotel_filter_selection_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }

           hotel_filter_selection_rv.addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1),
                    LinearLayoutManager.HORIZONTAL))
            hotel_filter_selection_rv.adapter = adapter
            adapter.updateItems(hotelFilterItems, filter.optionSelected.toSet())
        }
    }

    override fun resetSelection() {
        selectedOption = ParamFilterV2()
        adapter.clearSelection()
    }

    companion object {
        val LAYOUT = R.layout.layout_hotel_filter_selection
        const val SELECTION_STAR_TYPE = "star"
    }
}