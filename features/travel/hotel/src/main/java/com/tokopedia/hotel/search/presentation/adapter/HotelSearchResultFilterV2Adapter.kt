package com.tokopedia.hotel.search.presentation.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.search.data.model.FilterV2
import com.tokopedia.hotel.search.data.model.params.ParamFilterV2
import com.tokopedia.hotel.search.presentation.adapter.viewholder.*
import com.tokopedia.kotlin.extensions.view.inflateLayout

/**
 * @author by jessica on 12/08/20
 */

class HotelSearchResultFilterV2Adapter: RecyclerView.Adapter<HotelSearchResultFilterV2Adapter.FilterBaseViewHolder>(),
    OnSelectedFilterChangedListener{

    var filters: List<FilterV2> = listOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    var isSelectionWithOverflowLayout = false

    var selectedFilter = hashMapOf<String, List<String>>()

    val paramFilterV2: List<ParamFilterV2>
    get() = selectedFilter.filter { it.value.isNotEmpty() }.map {
            ParamFilterV2(it.key, it.value.toMutableList())
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterBaseViewHolder {
        return when (viewType) {
            SELECTION_VIEW_TYPE -> {
                FilterSelectionViewHolder(parent.inflateLayout(FilterSelectionViewHolder.LAYOUT), this, isSelectionWithOverflowLayout)
            }
            OPEN_RANGE_VIEW_TYPE -> {
                FilterOpenRangeViewHolder(parent.inflateLayout(FilterOpenRangeViewHolder.LAYOUT), this)
            }
            SELECTION_RANGE_TYPE -> {
                FilterSelectionRangeViewHolder(parent.inflateLayout(FilterSelectionRangeViewHolder.LAYOUT), this)
            }
            FILTER_SORT_TYPE -> {
                FilterSortViewHolder(parent.inflateLayout(FilterSortViewHolder.LAYOUT), this)
            }
            else -> {
                FilterSelectionViewHolder(parent.inflateLayout(FilterSelectionViewHolder.LAYOUT), this, isSelectionWithOverflowLayout)
            }
        }
    }

    override fun getItemCount(): Int = filters.size

    override fun onBindViewHolder(holder: FilterBaseViewHolder, position: Int) {
        holder.bind(filters[position])
    }

    override fun getItemViewType(position: Int): Int {
        return when (filters[position].type.toLowerCase()) {
            FilterV2.FILTER_TYPE_SELECTION -> SELECTION_VIEW_TYPE
            FilterV2.FILTER_TYPE_OPEN_RANGE -> OPEN_RANGE_VIEW_TYPE
            FilterV2.FILTER_TYPE_SELECTION_RANGE -> SELECTION_RANGE_TYPE
            FilterV2.FILTER_TYPE_SORT -> FILTER_SORT_TYPE
            else -> SELECTION_VIEW_TYPE
        }
    }

    abstract class FilterBaseViewHolder(view: View): RecyclerView.ViewHolder(view) {
        abstract var filterName: String
        abstract fun bind(filter: FilterV2)
    }

    companion object {
        const val SELECTION_VIEW_TYPE = 13
        const val OPEN_RANGE_VIEW_TYPE = 15
        const val SELECTION_RANGE_TYPE = 17
        const val FILTER_SORT_TYPE = 19
    }

    override fun onSelectedFilterChanged(name: String, filter: List<String>) {
        selectedFilter[name] = filter
    }
}