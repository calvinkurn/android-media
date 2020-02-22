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
    private var remains: MutableList<KeywordViewModel> = mutableListOf()
    var favoured: MutableList<KeywordItemViewModel> = mutableListOf()
    var manualKeywords: MutableList<KeywordItemViewModel> = mutableListOf()
    var ind: Int = 0
    private var SELECTED_KEYWORD = " Kata Kunci Pilihan"
    private var RECOMMENDED = "Rekomendasi"

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

    fun getSelectedItems(): List<KeywordItemViewModel> {
        var selected = mutableListOf<KeywordItemViewModel>()
        items.forEachIndexed { _, model ->
            if ((model is KeywordItemViewModel) && model.isChecked) {
                selected.add(model)
            }
        }
        return selected
    }

    fun addNewKeyword(it: KeywordItemViewModel): Boolean {
        if (items.size != 0 && items[0] is KeywordEmptyViewModel) {
            items.clear()
            items.add(0, KeywordGroupViewModel(SELECTED_KEYWORD))
        } else if (items.size != 0 && items[0] is KeywordGroupViewModel && (items[0] as KeywordGroupViewModel).title != SELECTED_KEYWORD) {
            items.add(0, KeywordGroupViewModel(SELECTED_KEYWORD))
        }
        favoured.forEachIndexed lit@{ index, _ ->
            if (favoured[index].data.keyword == it.data.keyword) {
                return true
                ind = index
                return@lit
            }
        }
        if (ind != 0)
            items.remove(favoured[ind])
        items.removeAll(favoured)
        favoured.add(it)
        items.addAll(1, favoured)
        it.isChecked = true
        manualKeywords.add(it)
        notifyDataSetChanged()
        return false
    }

    fun setSelectedKeywords(it: List<KeywordItemViewModel>) {
        favoured.clear()
        remains.clear()
        favoured.addAll(it)
        items.removeAll(favoured)
        items.forEach { index ->
            if (index is KeywordItemViewModel)
                remains.add(index)
        }
        items.clear()
        items.add(0, KeywordGroupViewModel(SELECTED_KEYWORD))
        items.addAll(favoured)
        if(remains.size!=0) {
            items.add(KeywordGroupViewModel(RECOMMENDED))
            items.addAll(remains)
        }
        notifyDataSetChanged()
    }


    fun setSelectedList(selectedKeywords: MutableList<String>) {
        items.forEachIndexed { _, key ->
            if (key is KeywordItemViewModel) {
                key.isChecked = false
                selectedKeywords.forEach {
                    if (key is KeywordItemViewModel) {
                        if (key.data.keyword == it) {
                            key.isChecked = true
                        }
                    }
                }
            }
        }
        notifyDataSetChanged()
    }

    fun addManual(list: MutableList<KeywordItemViewModel>) {
        items.clear()
        manualKeywords.addAll(list)
        items.add(0, KeywordGroupViewModel(SELECTED_KEYWORD))
        items.addAll(list)
        notifyDataSetChanged()
    }
}
