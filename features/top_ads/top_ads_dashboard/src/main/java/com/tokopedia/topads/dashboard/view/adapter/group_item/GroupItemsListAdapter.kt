package com.tokopedia.topads.dashboard.view.adapter.group_item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.view.adapter.group_item.viewholder.GroupItemsViewHolder
import com.tokopedia.topads.dashboard.view.adapter.group_item.viewmodel.GroupItemsItemViewModel
import com.tokopedia.topads.dashboard.view.adapter.group_item.viewmodel.GroupItemsViewModel

/**
 * Created by Pika on 2/6/20.
 */

class GroupItemsListAdapter(private val typeFactory: GroupItemsAdapterTypeFactory) : RecyclerView.Adapter<GroupItemsViewHolder<GroupItemsViewModel>>() {


    var items: MutableList<GroupItemsViewModel> = mutableListOf()
    private var selectedMode = false
    private var fromSearch = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupItemsViewHolder<GroupItemsViewModel> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.holder(viewType, view) as GroupItemsViewHolder<GroupItemsViewModel>
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

    private fun clearData(selectedMode: Boolean) {
        if (!selectedMode){
            items.forEach {
                if (it is GroupItemsItemViewModel) {
                   it.isChecked = false
                }
            }
        }
    }

    override fun onBindViewHolder(holder: GroupItemsViewHolder<GroupItemsViewModel>, position: Int) {

        holder.bind(items[position], selectedMode, fromSearch)
    }

    fun getSelectedItems(): MutableList<GroupItemsItemViewModel> {
        val list: MutableList<GroupItemsItemViewModel> = mutableListOf()
        items.forEach {
            if (it is GroupItemsItemViewModel) {
                if (it.isChecked) {
                    list.add(it)

                }
            }
        }
        return list
    }

    fun setEmptyView(fromSearch: Boolean) {
        this.fromSearch = fromSearch
        notifyDataSetChanged()
    }

}


