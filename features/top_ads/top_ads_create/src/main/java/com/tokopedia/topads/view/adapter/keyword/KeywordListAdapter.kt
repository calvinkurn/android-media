package com.tokopedia.topads.view.adapter.keyword

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.view.adapter.keyword.viewholder.KeywordViewHolder
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordViewModel
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordEmptyViewModel
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordGroupViewModel
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordItemViewModel

/**
 * Author errysuprayogi on 14,November,2019
 */
class KeywordListAdapter(val typeFactory: KeywordListAdapterTypeFactory) : RecyclerView.Adapter<KeywordViewHolder<KeywordViewModel>>() {


    var items: MutableList<KeywordViewModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder<KeywordViewModel> {
        if (parent != null) {
            val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            return typeFactory.holder(viewType, view) as KeywordViewHolder<KeywordViewModel>
        }
        throw RuntimeException("Parent is null")
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type(typeFactory)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: KeywordViewHolder<KeywordViewModel>, position: Int) {
        holder.bind(items[position])
    }

    fun getSelectedItems(): List<KeywordViewModel> {
        var selected = mutableListOf<KeywordViewModel>()
        items.forEachIndexed { index, model ->
            if((model is KeywordItemViewModel) && model.isChecked) {
                selected.add(model)
            }
        }
        return selected
    }

    fun addNewKeyword(it: KeywordViewModel) {
        if(items.get(0) is KeywordEmptyViewModel){
            items.clear()
            items.add(0, KeywordGroupViewModel("Kata Kunci Pilihan"))
            items.add(1, it)
        } else if (items.get(0) is KeywordGroupViewModel && (items.get(0) as KeywordGroupViewModel).title.equals("Kata Kunci Pilihan")){
            items.add(1, it)
        } else {
            items.add(0, KeywordGroupViewModel("Kata Kunci Pilihan"))
            items.add(1, it)
        }
        notifyDataSetChanged()
    }

}