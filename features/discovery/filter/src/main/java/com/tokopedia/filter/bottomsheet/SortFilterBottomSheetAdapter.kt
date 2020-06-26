package com.tokopedia.filter.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

internal class SortFilterBottomSheetAdapter(
        private val sortFilterBottomSheetTypeFactory: SortFilterBottomSheetTypeFactory
): RecyclerView.Adapter<AbstractViewHolder<Visitable<*>>>() {

    private val sortFilterList = mutableListOf<Visitable<SortFilterBottomSheetTypeFactory>>()

    fun setSortFilterList(sortFilterList: List<Visitable<SortFilterBottomSheetTypeFactory>>) {
        clearPreviousSortFilterList()
        insertNewSortFilterList(sortFilterList)
    }

    private fun clearPreviousSortFilterList() {
        val previousSize = this.sortFilterList.size
        this.sortFilterList.clear()
        notifyItemRangeRemoved(0, previousSize)
    }

    private fun insertNewSortFilterList(sortFilterList: List<Visitable<SortFilterBottomSheetTypeFactory>>) {
        this.sortFilterList.addAll(sortFilterList)
        notifyItemRangeInserted(0, sortFilterList.size)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Visitable<*>> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return sortFilterBottomSheetTypeFactory.createViewHolder(view, viewType) as AbstractViewHolder<Visitable<*>>
    }

    override fun getItemCount(): Int {
        return sortFilterList.size
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
        holder.bind(sortFilterList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return sortFilterList[position].type(sortFilterBottomSheetTypeFactory)
    }
}