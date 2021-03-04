package com.tokopedia.topads.dashboard.view.adapter.negkeyword

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewholder.NegKeywordViewHolder
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewmodel.NegKeywordItemModel
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewmodel.NegKeywordModel


class NegKeywordAdapter(val typeFactory: NegKeywordAdapterTypeFactory) : RecyclerView.Adapter<NegKeywordViewHolder<NegKeywordModel>>() {
    private var isSelectMode = false
    private var fromSearch = false
    private var fromHeadline = false


    var items: MutableList<NegKeywordModel> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NegKeywordViewHolder<NegKeywordModel> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.holder(viewType, view) as NegKeywordViewHolder<NegKeywordModel>
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

    override fun onBindViewHolder(holder: NegKeywordViewHolder<NegKeywordModel>, position: Int) {
        holder.bind(items[position], isSelectMode, fromSearch, fromHeadline)
    }

    fun getSelectedItems(): MutableList<NegKeywordItemModel> {
        val list: MutableList<NegKeywordItemModel> = mutableListOf()
        items.forEach {
            if (it is NegKeywordItemModel) {
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
                if (it is NegKeywordItemModel) {
                    it.isChecked = false
                }
            }
        }
    }

    fun setEmptyView(fromSearch: Boolean,fromHeadline:Boolean = false) {
        this.fromHeadline = fromHeadline
        this.fromSearch = fromSearch
        notifyDataSetChanged()
    }
}
