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

    var filterSearchList: List<FilterSearchUiModel>? = null

    fun setFilterSearch(filterSearchList: List<FilterSearchUiModel>) {
        this.filterSearchList = filterSearchList
    }

    fun updatedSortFilter(position: Int) {
        val itemSelected = filterSearchList?.getOrNull(position)

        filterSearchList?.filter {
            it.isSelected
        }?.filterNot { it == itemSelected }?.onEach { it.isSelected = false }

        itemSelected?.isSelected = true
        notifyDataSetChanged()
    }

    fun resetSortFilter() {
        filterSearchList?.mapIndexed { index, sortItemUiModel ->
            sortItemUiModel.isSelected = index == 0
        }
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

    class FilterSearchViewHolder(itemView: View, private val filterSearchListener: FilterSearchListener): RecyclerView.ViewHolder(itemView) {
        fun bind(data: FilterSearchUiModel) {
            with(itemView) {
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
                        filterSearchListener.onFilterItemClicked(data.keyword.orEmpty(), chipType.orEmpty(), adapterPosition)
                    }
                }
            }
        }
    }
}