package com.tokopedia.hotel.search.presentation.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.search.data.model.FilterV2
import com.tokopedia.hotel.search.data.model.params.ParamFilterV2
import com.tokopedia.hotel.search.presentation.adapter.viewholder.FilterOpenRangeViewHolder
import com.tokopedia.hotel.search.presentation.adapter.viewholder.FilterSelectionRangeViewHolder
import com.tokopedia.hotel.search.presentation.adapter.viewholder.FilterSelectionViewHolder
import com.tokopedia.kotlin.extensions.view.inflateLayout

/**
 * @author by jessica on 12/08/20
 */

class HotelSearchResultFilterV2Adapter: RecyclerView.Adapter<HotelSearchResultFilterV2Adapter.FilterBaseViewHolder>() {

    var filters: List<FilterV2> = listOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterBaseViewHolder {
        return when (viewType) {
            SELECTION_VIEW_TYPE -> {
                FilterSelectionViewHolder(parent.inflateLayout(FilterSelectionViewHolder.LAYOUT))
            }
            OPEN_RANGE_VIEW_TYPE -> {
                FilterOpenRangeViewHolder(parent.inflateLayout(FilterOpenRangeViewHolder.LAYOUT))
            }
            SELECTION_RANGE_TYPE -> {
                FilterSelectionRangeViewHolder(parent.inflateLayout(FilterSelectionRangeViewHolder.LAYOUT))
            }
            else -> {
                FilterSelectionViewHolder(parent.inflateLayout(FilterSelectionViewHolder.LAYOUT))
            }
        }
    }

    override fun getItemCount(): Int = filters.size

    override fun onBindViewHolder(holder: FilterBaseViewHolder, position: Int) {
        holder.bind(filters[position])
    }

    override fun getItemViewType(position: Int): Int {
        return when (filters[position].type.toLowerCase()) {
            "selection" -> SELECTION_VIEW_TYPE
            "open_range" -> OPEN_RANGE_VIEW_TYPE
            "selection_range" -> SELECTION_RANGE_TYPE
            else -> SELECTION_VIEW_TYPE
        }
    }

    abstract class FilterBaseViewHolder(view: View): RecyclerView.ViewHolder(view) {
        abstract var selectedOption: ParamFilterV2
        abstract fun bind(filter: FilterV2)
        abstract fun resetSelection()
    }

    companion object {
        const val SELECTION_VIEW_TYPE = 13
        const val OPEN_RANGE_VIEW_TYPE = 15
        const val SELECTION_RANGE_TYPE = 17
    }
}