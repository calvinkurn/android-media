package com.tokopedia.topads.view.adapter.keyword

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.view.adapter.keyword.viewholder.KeywordViewHolder
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordViewModel
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordEmptyViewModel
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordGroupViewModel
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordItemViewModel
import java.util.*

/**
 * Author errysuprayogi on 14,November,2019
 */
class KeywordListAdapter(val typeFactory: KeywordListAdapterTypeFactory) : RecyclerView.Adapter<KeywordViewHolder<KeywordViewModel>>() {


    var items: MutableList<KeywordViewModel> = mutableListOf()
    var remains: MutableList<KeywordViewModel> = mutableListOf()
    private var remaining = mutableListOf<KeywordViewModel>()

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
            else
                remaining.add(model)
        }

        return selected
    }

    fun addNewKeyword(it: KeywordViewModel) {
        items.forEach { index->
            if(index is KeywordItemViewModel){
               if( index.data.keyword == (it as KeywordItemViewModel).data.keyword){
                   if(index.isChecked==true){
                      // Toast.makeText(this,"already added ",Toast.LENGTH_SHORT)
                       return
                   }
                   else
                   items.remove(it)
               }
            }
        }

        if (items.contains(it)) {
            items.remove(it)
        }
        if (items.size != 0 && items[0] is KeywordEmptyViewModel) {
            items.clear()
            items.add(0, KeywordGroupViewModel("Kata Kunci Pilihan"))
            items.add(1, it)
        } else if (items.size != 0 && items[0] is KeywordGroupViewModel && (items.get(0) as KeywordGroupViewModel).title.equals("Kata Kunci Pilihan")) {
            items.add(1, it)
        } else {
            items.add(0, KeywordGroupViewModel("Kata Kunci Pilihan"))
            items.add(1, it)
        }
        (it as KeywordItemViewModel).isChecked = true

        notifyItemInserted(0)
    }

    fun addNewKeywords(it: List<KeywordViewModel>) {
        remains.clear()
        items.removeAll(it)
        items.forEach { index ->
            if (index is KeywordItemViewModel) {
                remains.add(index)
            }
        }
        items.clear()
        items.add(0, KeywordGroupViewModel("Kata Kunci Pilihan"))
        items.addAll(1, it)
        items.forEach { index ->
            if (index is KeywordItemViewModel) {
                index.isChecked = true
            }
        }
        items.add(KeywordGroupViewModel("Rekomendasi"))
        Collections.sort(remains as List<KeywordItemViewModel>, kotlin.Comparator { o1: KeywordItemViewModel, o2: KeywordItemViewModel ->
            when {
                (o1.data.totalSearch > o2.data.totalSearch) -> 0

                else -> 1
            }
        }
        )
        items.addAll(remains)
        notifyDataSetChanged()
    }
}

