package com.tokopedia.topads.headline.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.common.data.response.groupitem.DataItem
import com.tokopedia.topads.dashboard.data.model.CountDataItem
import com.tokopedia.topads.headline.view.adapter.viewholder.HeadLineItemsViewHolder
import com.tokopedia.topads.headline.view.adapter.viewmodel.HeadLineItemsItemViewModel
import com.tokopedia.topads.headline.view.adapter.viewmodel.HeadLineItemsViewModel

/**
 * Created by Pika on 16/10/20.
 */

class HeadLineItemsListAdapter(private val typeFactory: HeadLineItemsAdapterTypeFactory) : RecyclerView.Adapter<HeadLineItemsViewHolder<HeadLineItemsViewModel>>() {

    var items: MutableList<HeadLineItemsViewModel> = mutableListOf()
    var countList: MutableList<CountDataItem> = mutableListOf()
    private var selectedMode = false
    private var fromSearch = false
    var statsData: MutableList<DataItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadLineItemsViewHolder<HeadLineItemsViewModel> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.holder(viewType, view) as HeadLineItemsViewHolder<HeadLineItemsViewModel>
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
                if (it is HeadLineItemsItemViewModel) {
                    it.isChecked = false
                }
            }
        }
    }

    override fun onBindViewHolder(holder: HeadLineItemsViewHolder<HeadLineItemsViewModel>, position: Int) {
        holder.bind(items[position], selectedMode, fromSearch, statsData, countList)
    }

    fun getSelectedItems(): MutableList<HeadLineItemsItemViewModel> {
        val list: MutableList<HeadLineItemsItemViewModel> = mutableListOf()
        items.forEach {
            if (it is HeadLineItemsItemViewModel) {
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


