package com.tokopedia.topads.dashboard.view.adapter.non_group_item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewholder.NonGroupItemsViewHolder
import com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewmodel.NonGroupItemsItemViewModel
import com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewmodel.NonGroupItemsViewModel

/**
 * Created by Pika on 2/6/20.
 */

class NonGroupItemsListAdapter(private val typeFactory: NonGroupItemsAdapterTypeFactory) : RecyclerView.Adapter<NonGroupItemsViewHolder<NonGroupItemsViewModel>>() {


    var items: MutableList<NonGroupItemsViewModel> = mutableListOf()
    var statsData: MutableList<WithoutGroupDataItem> = mutableListOf()
    var selectedMode = false
    var fromSearch = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NonGroupItemsViewHolder<NonGroupItemsViewModel> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.holder(viewType, view) as NonGroupItemsViewHolder<NonGroupItemsViewModel>
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type(typeFactory)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun setSelectMode(isSelect: Boolean) {
        selectedMode = isSelect
        clearData(selectedMode)
        notifyDataSetChanged()

    }

    override fun onBindViewHolder(holder: NonGroupItemsViewHolder<NonGroupItemsViewModel>, position: Int) {
        holder.bind(items[position], selectedMode, fromSearch ,statsData)
    }

    fun getSelectedItems(): MutableList<NonGroupItemsItemViewModel> {
        val list: MutableList<NonGroupItemsItemViewModel> = mutableListOf()
        items.forEach {
            if (it is NonGroupItemsItemViewModel) {
                if (it.isChecked) {
                    list.add(it)

                }
            }
        }
        return list
    }

    private fun clearData(selectedMode: Boolean) {
        if (!selectedMode){
            items.forEach {
                if (it is NonGroupItemsItemViewModel) {
                    it.isChecked = false
                }
            }
        }
    }

    fun setEmptyView(fromSearch: Boolean) {
        this.fromSearch = fromSearch
        notifyDataSetChanged()
    }

    fun setstatistics(data: List<WithoutGroupDataItem>) {
        statsData = data.toMutableList()
        notifyDataSetChanged()
    }

}
