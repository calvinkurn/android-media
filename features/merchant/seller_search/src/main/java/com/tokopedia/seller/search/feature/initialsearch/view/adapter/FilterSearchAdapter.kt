package com.tokopedia.seller.search.feature.initialsearch.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.model.filter.FilterSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.FilterSearchListener
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.item_chips_filter.view.*

class FilterSearchAdapter(private val filterSearchListener: FilterSearchListener): RecyclerView.Adapter<FilterSearchAdapter.FilterSearchViewHolder>() {

    private var filterSearchList: List<FilterSearchUiModel>? = null

    fun setFilterSearch(filterSearchList: List<FilterSearchUiModel>) {
        this.filterSearchList = filterSearchList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterSearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chips_filter, parent, false)
        return FilterSearchViewHolder(view, filterSearchListener)
    }

    override fun getItemCount(): Int {
        return filterSearchList?.size ?: 0
    }

    override fun onBindViewHolder(holder: FilterSearchViewHolder, position: Int) {
        filterSearchList?.get(position)?.let { holder.bind(it) }
    }

    class FilterSearchViewHolder(private val itemViewFilter: View, private val filterSearchListener: FilterSearchListener): RecyclerView.ViewHolder(itemViewFilter) {
        fun bind(data: FilterSearchUiModel) {
            with(itemViewFilter) {
                chipsItem.apply {
                    centerText = true
                    chipText = data.title
                    chipSize = ChipsUnify.SIZE_MEDIUM
                    chipType = if(data.isSelected) {
                        ChipsUnify.TYPE_SELECTED
                    } else {
                        ChipsUnify.TYPE_NORMAL
                    }
                    setOnClickListener {
                        filterSearchListener.onFilterItemClicked(data.title.orEmpty(), chipType.orEmpty(), adapterPosition)
                    }
                }
            }
        }
    }
}