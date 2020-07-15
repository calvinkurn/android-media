package com.tokopedia.topads.dashboard.view.adapter.keyword

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewholder.KeywordViewHolder
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel.KeywordItemViewModel
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel.KeywordViewModel


class KeywordAdapter(val typeFactory: KeywordAdapterTypeFactory): RecyclerView.Adapter<KeywordViewHolder<KeywordViewModel>>() {

    private var isSelectMode = false
    private var fromSearch = false

    var items: MutableList<KeywordViewModel> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder<KeywordViewModel> {
            val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            return typeFactory.holder(viewType, view) as KeywordViewHolder<KeywordViewModel>
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type(typeFactory)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun setSelectMode(isSelectMode:Boolean){
        this.isSelectMode = isSelectMode
        clearData(isSelectMode)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: KeywordViewHolder<KeywordViewModel>, position: Int) {
        holder.bind(items[position] ,isSelectMode ,fromSearch)
    }

    fun getSelectedItems(): MutableList<KeywordItemViewModel> {
        var list: MutableList<KeywordItemViewModel> = mutableListOf()
        items.forEach {
            if (it is KeywordItemViewModel) {
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
                if (it is KeywordItemViewModel) {
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
