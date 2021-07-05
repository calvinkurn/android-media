package com.tokopedia.topads.dashboard.view.adapter.group_item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.common.data.response.groupitem.DataItem
import com.tokopedia.topads.dashboard.data.model.CountDataItem
import com.tokopedia.topads.dashboard.view.adapter.group_item.viewholder.GroupItemsViewHolder
import com.tokopedia.topads.dashboard.view.adapter.group_item.viewmodel.GroupItemsItemModel
import com.tokopedia.topads.dashboard.view.adapter.group_item.viewmodel.GroupItemsModel

/**
 * Created by Pika on 2/6/20.
 */

class GroupItemsListAdapter(private val typeFactory: GroupItemsAdapterTypeFactory) : RecyclerView.Adapter<GroupItemsViewHolder<GroupItemsModel>>() {


    var items: MutableList<GroupItemsModel> = mutableListOf()
    var countList: MutableList<CountDataItem> = mutableListOf()
    private var selectedMode = false
    private var fromSearch = false
    var statsData: MutableList<DataItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupItemsViewHolder<GroupItemsModel> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.holder(viewType, view) as GroupItemsViewHolder<GroupItemsModel>
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
        if (!selectedMode) {
            items.forEach {
                if (it is GroupItemsItemModel) {
                    it.isChecked = false
                }
            }
        }
    }

    override fun onBindViewHolder(holder: GroupItemsViewHolder<GroupItemsModel>, position: Int) {

        holder.bind(items[position], selectedMode, fromSearch, statsData, countList)
    }

    fun getSelectedItems(): MutableList<GroupItemsItemModel> {
        val list: MutableList<GroupItemsItemModel> = mutableListOf()
        items.forEach {
            if (it is GroupItemsItemModel) {
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

    fun setstatistics(data: List<DataItem>) {
        statsData = data.toMutableList()
        notifyDataSetChanged()
    }

    fun setItemCount(counList: List<CountDataItem>) {
        countList = counList.toMutableList()
        notifyDataSetChanged()
    }

}


