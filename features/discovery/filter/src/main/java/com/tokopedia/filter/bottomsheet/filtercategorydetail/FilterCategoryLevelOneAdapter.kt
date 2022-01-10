package com.tokopedia.filter.bottomsheet.filtercategorydetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.filter.R

internal class FilterCategoryLevelOneAdapter(
        private val callback: Callback
) : RecyclerView.Adapter<FilterCategoryLevelOneViewHolder>(), FilterCategoryLevelOneViewHolder.HeaderViewHolderCallback {

    private val filterCategoryLevelOneViewModelList = mutableListOf<FilterCategoryLevelOneViewModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterCategoryLevelOneViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filter_category_detail_level_one_view_holder, parent, false)
        return FilterCategoryLevelOneViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: FilterCategoryLevelOneViewHolder, position: Int) {
        holder.bind(filterCategoryLevelOneViewModelList[position])
    }

    override fun getItemCount(): Int {
        return filterCategoryLevelOneViewModelList.size
    }

    fun setList(filterCategoryLevelOneViewModelList: List<FilterCategoryLevelOneViewModel>) {
        this.filterCategoryLevelOneViewModelList.clear()
        this.filterCategoryLevelOneViewModelList.addAll(filterCategoryLevelOneViewModelList)
        notifyItemRangeInserted(0, filterCategoryLevelOneViewModelList.size)
    }

    fun scrollToSelected(recyclerView: RecyclerView?) {
        if (recyclerView == null) return

        val selectedIndex = filterCategoryLevelOneViewModelList.indexOfFirst { it.isSelected }
        if (isPositionInvalid(selectedIndex)) return

        recyclerView.scrollToPosition(selectedIndex)
    }

    fun scrollToSelectedIfNotFullyVisible(recyclerView: RecyclerView?, position: Int) {
        if (isPositionInvalid(position)) return
        if (!filterCategoryLevelOneViewModelList[position].isSelected) return

        val layoutManager = recyclerView?.layoutManager ?: return
        if (layoutManager is LinearLayoutManager) {
            if (layoutManager.findLastCompletelyVisibleItemPosition() < position
                    || layoutManager.findFirstCompletelyVisibleItemPosition() > position) {
                recyclerView.scrollToPosition(position)
            }
        }
    }

    private fun isPositionInvalid(position: Int) =
            position < 0 || position >= filterCategoryLevelOneViewModelList.size

    override fun onHeaderItemClick(position: Int) {
        if (isPositionInvalid(position)) return
        callback.onHeaderItemClick(filterCategoryLevelOneViewModelList[position])
    }

    internal interface Callback {
        fun onHeaderItemClick(filterCategoryLevelOneViewModel: FilterCategoryLevelOneViewModel)
    }
}