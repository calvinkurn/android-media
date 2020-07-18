package com.tokopedia.topads.dashboard.view.adapter.negkeyword

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewholder.NegKeywordViewHolder
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewmodel.NegKeywordItemViewModel
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewmodel.NegKeywordViewModel


class NegKeywordAdapter(val typeFactory: NegKeywordAdapterTypeFactory) : RecyclerView.Adapter<NegKeywordViewHolder<NegKeywordViewModel>>() {
    private var isSelectMode = false
    private var fromSearch = false

    var items: MutableList<NegKeywordViewModel> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NegKeywordViewHolder<NegKeywordViewModel> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.holder(viewType, view) as NegKeywordViewHolder<NegKeywordViewModel>
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

    override fun onBindViewHolder(holder: NegKeywordViewHolder<NegKeywordViewModel>, position: Int) {
        holder.bind(items[position], isSelectMode, fromSearch)
    }

    fun getSelectedItems(): MutableList<NegKeywordItemViewModel> {
        val list: MutableList<NegKeywordItemViewModel> = mutableListOf()
        items.forEach {
            if (it is NegKeywordItemViewModel) {
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
                if (it is NegKeywordItemViewModel) {
                    it.isChecked = false
                }
            }
        }
    }

    fun setEmptyView(fromSearch: Boolean) {
        this.fromSearch = fromSearch
        notifyDataSetChanged()
    }
}
