package com.tokopedia.hotel.search_map.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.LayoutHotelFilterSelectionBinding
import com.tokopedia.hotel.search_map.data.model.FilterV2
import com.tokopedia.hotel.search_map.presentation.adapter.HotelSearchResultFilterAdapter
import com.tokopedia.hotel.search_map.presentation.adapter.HotelSearchResultFilterV2Adapter
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * @author by jessica on 12/08/20
 */

class FilterSortViewHolder(view: View, val listener: OnSelectedFilterChangedListener) :
        HotelSearchResultFilterV2Adapter.FilterBaseViewHolder(view), HotelSearchResultFilterAdapter.ActionListener {

    private val binding = LayoutHotelFilterSelectionBinding.bind(view)

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

        with(binding) {
            hotelFilterSelectionTitle.text = filter.displayName
            var hotelFilterItems = filter.options.map {
               HotelSearchResultFilterAdapter.HotelFilterItem(it, it)
            }

            hotelFilterSelectionRv.layoutManager = ChipsLayoutManager.newBuilder(root.context)
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                    .build()

            while (hotelFilterSelectionRv.itemDecorationCount > 0) {
                hotelFilterSelectionRv.removeItemDecorationAt(0)
            }
            hotelFilterSelectionRv.addItemDecoration(SpaceItemDecoration(root.resources.getDimensionPixelSize(unifyprinciplesR.dimen.layout_lvl1),
                    LinearLayoutManager.HORIZONTAL))

            hotelFilterSelectionRv.adapter = adapter
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
