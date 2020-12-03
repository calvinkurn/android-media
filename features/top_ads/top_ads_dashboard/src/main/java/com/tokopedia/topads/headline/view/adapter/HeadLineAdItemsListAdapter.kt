package com.tokopedia.topads.headline.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.common.data.response.groupitem.DataItem
import com.tokopedia.topads.dashboard.data.model.CountDataItem
import com.tokopedia.topads.headline.view.adapter.viewholder.HeadLineAdItemsViewHolder
import com.tokopedia.topads.headline.view.adapter.viewmodel.HeadLineAdItemsItemViewModel
import com.tokopedia.topads.headline.view.adapter.viewmodel.HeadLineAdItemsViewModel

/**
 * Created by Pika on 16/10/20.
 */

class HeadLineAdItemsListAdapter(private val typeFactory: HeadLineAdItemsAdapterTypeFactory) : RecyclerView.Adapter<HeadLineAdItemsViewHolder<HeadLineAdItemsViewModel>>() {


    var items: MutableList<HeadLineAdItemsViewModel> = mutableListOf()
    var countList: MutableList<CountDataItem> = mutableListOf()
    private var selectedMode = false
    private var fromSearch = false
    private var selectedText = ""
    var statsData: MutableList<DataItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadLineAdItemsViewHolder<HeadLineAdItemsViewModel> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.holder(viewType, view) as HeadLineAdItemsViewHolder<HeadLineAdItemsViewModel>
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
                if (it is HeadLineAdItemsItemViewModel) {
                    it.isChecked = false
                }
            }
        }
    }

    override fun onBindViewHolder(holder: HeadLineAdItemsViewHolder<HeadLineAdItemsViewModel>, position: Int) {
        holder.bind(items[position], selectedMode, fromSearch, statsData, countList ,selectedText)
    }

    fun getSelectedItems(): MutableList<HeadLineAdItemsItemViewModel> {
        val list: MutableList<HeadLineAdItemsItemViewModel> = mutableListOf()
        items.forEach {
            if (it is HeadLineAdItemsItemViewModel) {
                if (it.isChecked) {
                    list.add(it)
                }
            }
        }
        return list
    }

    fun setEmptyView(fromSearch: Boolean, selectedText: String = "") {
        this.fromSearch = fromSearch
        this.selectedText = selectedText
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


