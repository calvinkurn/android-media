package com.tokopedia.hotel.search_map.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.presentation.widget.SpanningLinearLayoutManager
import com.tokopedia.hotel.databinding.LayoutHotelFilterSelectionBinding
import com.tokopedia.hotel.search_map.data.model.FilterStarEnum
import com.tokopedia.hotel.search_map.data.model.FilterV2
import com.tokopedia.hotel.search_map.presentation.adapter.HotelSearchResultFilterAdapter
import com.tokopedia.hotel.search_map.presentation.adapter.HotelSearchResultFilterV2Adapter
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * @author by jessica on 12/08/20
 */

class FilterSelectionViewHolder(view: View, val listener: OnSelectedFilterChangedListener, var isOverFlowLayout: Boolean = false) :
    HotelSearchResultFilterV2Adapter.FilterBaseViewHolder(view),
    HotelSearchResultFilterAdapter.ActionListener {

    private val binding = LayoutHotelFilterSelectionBinding.bind(view)

    override var filterName: String = ""

    private val adapter: HotelSearchResultFilterAdapter by lazy {
        HotelSearchResultFilterAdapter(HotelSearchResultFilterAdapter.MODE_MULTIPLE, this)
    }

    override fun bind(filter: FilterV2) {
        filterName = filter.name

        with(binding) {
            hotelFilterSelectionTitle.text = filter.displayName
            var hotelFilterItems = filter.options.map {
                HotelSearchResultFilterAdapter.HotelFilterItem(it, it, filter.name == SELECTION_STAR_TYPE)
            }

            if (filter.name == SELECTION_STAR_TYPE) {
                if (hotelFilterItems.isEmpty()) {
                    hotelFilterItems = listOf(
                        FilterStarEnum.STAR_ONE.value,
                        FilterStarEnum.STAR_TWO.value,
                        FilterStarEnum.STAR_THREE.value,
                        FilterStarEnum.STAR_FOUR.value,
                        FilterStarEnum.STAR_FIVE.value
                    ).map {
                        HotelSearchResultFilterAdapter.HotelFilterItem(it, it, filter.name == SELECTION_STAR_TYPE)
                    }
                }
                hotelFilterSelectionRv.layoutManager = SpanningLinearLayoutManager(
                    root.context,
                    LinearLayoutManager.HORIZONTAL,
                    false,
                    root.resources.getDimensionPixelSize(unifyprinciplesR.dimen.spacing_lvl2)
                )
            } else {
                if (!isOverFlowLayout) {
                    hotelFilterSelectionRv.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)
                } else {
                    hotelFilterSelectionRv.layoutManager = ChipsLayoutManager.newBuilder(root.context)
                        .setOrientation(ChipsLayoutManager.HORIZONTAL)
                        .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                        .build()
                }
            }

            while (hotelFilterSelectionRv.itemDecorationCount > 0) {
                hotelFilterSelectionRv.removeItemDecorationAt(0)
            }
            hotelFilterSelectionRv.addItemDecoration(
                SpaceItemDecoration(
                    root.resources.getDimensionPixelSize(unifyprinciplesR.dimen.layout_lvl1),
                    LinearLayoutManager.HORIZONTAL
                )
            )

            hotelFilterSelectionRv.adapter = adapter
            adapter.updateItems(hotelFilterItems, filter.optionSelected.toSet())
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_hotel_filter_selection
        const val SELECTION_STAR_TYPE = "star"
    }

    override fun onSelectedFilterChanged(selectedItems: List<String>) {
        listener.onSelectedFilterChanged(filterName, selectedItems.toMutableList())
    }
}
