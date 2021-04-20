package com.tokopedia.hotel.search.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.hotel.R
import com.tokopedia.hotel.search.data.model.FilterV2
import com.tokopedia.hotel.search.data.model.params.ParamFilterV2
import com.tokopedia.hotel.search.presentation.adapter.HotelSearchResultFilterAdapter
import com.tokopedia.hotel.search.presentation.adapter.HotelSearchResultFilterV2Adapter
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import kotlinx.android.synthetic.main.layout_hotel_filter_selection.view.*

/**
 * @author by jessica on 12/08/20
 */

class FilterSortViewHolder(view: View, val listener: OnSelectedFilterChangedListener) :
        HotelSearchResultFilterV2Adapter.FilterBaseViewHolder(view), HotelSearchResultFilterAdapter.ActionListener {

    override var filterName: String = "sort"

    private var defaultOption: String = ""

    private val adapter: HotelSearchResultFilterAdapter by lazy {
        HotelSearchResultFilterAdapter(HotelSearchResultFilterAdapter.MODE_SINGLE, this)
    }

    override fun bind(filter: FilterV2) {
        defaultOption = filter.defaultOption
        if (filter.optionSelected.isNotEmpty()) {
            listener.onSelectedFilterChanged(filterName, filter.optionSelected.toMutableList())
        }
        else {
            filter.optionSelected = listOf(defaultOption)
            listener.onSelectedFilterChanged(filterName, listOf(filter.defaultOption))
        }

        with(itemView) {
            hotel_filter_selection_title.text = filter.displayName
            var hotelFilterItems = filter.options.map {
                HotelSearchResultFilterAdapter.HotelFilterItem(it, it)
            }

            hotel_filter_selection_rv.layoutManager = ChipsLayoutManager.newBuilder(context)
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                    .build()

            while (hotel_filter_selection_rv.itemDecorationCount > 0) {
                hotel_filter_selection_rv.removeItemDecorationAt(0)
            }
            hotel_filter_selection_rv.addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1),
                    LinearLayoutManager.HORIZONTAL))

            hotel_filter_selection_rv.adapter = adapter
            adapter.updateItems(hotelFilterItems, filter.optionSelected.toSet())
        }
    }

    override fun onSelectedFilterChanged(selectedItems: List<String>) {
        listener.onSelectedFilterChanged(filterName, selectedItems.toMutableList())
    }

    companion object {
        val LAYOUT = R.layout.layout_hotel_filter_selection
    }
}