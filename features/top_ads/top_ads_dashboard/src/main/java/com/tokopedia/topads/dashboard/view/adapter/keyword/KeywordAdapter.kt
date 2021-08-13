package com.tokopedia.topads.dashboard.view.adapter.keyword

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewholder.KeywordViewHolder
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel.KeywordItemModel
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel.KeywordModel


class KeywordAdapter(val typeFactory: KeywordAdapterTypeFactory) : RecyclerView.Adapter<KeywordViewHolder<KeywordModel>>() {

    private var isSelectMode = false
    private var fromSearch = false
    private var fromHeadline = false

    var items: MutableList<KeywordModel> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder<KeywordModel> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.holder(viewType, view) as KeywordViewHolder<KeywordModel>
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type(typeFactory)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun setSelectMode(isSelectMode: Boolean) {
        this.isSelectMode = isSelectMode
        clearData(isSelectMode)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: KeywordViewHolder<KeywordModel>, position: Int) {
        holder.bind(items[position], isSelectMode, fromSearch)
    }

    fun getSelectedItems(): MutableList<KeywordItemModel> {
        var list: MutableList<KeywordItemModel> = mutableListOf()
        items.forEach {
            if (it is KeywordItemModel) {
                if (it.isChecked) {
                    list.add(it)

                }
            }
        }
        return list
    }

    private fun clearData(selectedMode: Boolean) {
        if (!selectedMode) {
            items.forEach {
                if (it is KeywordItemModel) {
                    it.isChecked = false
                }
            }
        }
    }

    fun setEmptyView(fromSearch: Boolean, fromHeadline: Boolean = false) {
        this.fromHeadline = fromHeadline
        this.fromSearch = fromSearch
        notifyDataSetChanged()
    }

}
