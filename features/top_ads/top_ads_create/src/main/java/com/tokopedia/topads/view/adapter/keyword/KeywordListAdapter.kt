package com.tokopedia.topads.view.adapter.keyword

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.topads.view.adapter.keyword.viewholder.KeywordViewHolder
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordViewModel
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordEmptyViewModel
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordGroupViewModel
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordItemViewModel
import java.util.*
import kotlin.collections.HashSet

/**
 * Author errysuprayogi on 14,November,2019
 */
class KeywordListAdapter(val typeFactory: KeywordListAdapterTypeFactory) : RecyclerView.Adapter<KeywordViewHolder<KeywordViewModel>>() {


    var items: MutableList<KeywordViewModel> = mutableListOf()
    var remains: MutableList<KeywordViewModel> = mutableListOf()
    var favoured : MutableList<KeywordItemViewModel> = mutableListOf()
    var ind : Int =0

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
        items.forEachIndexed { index, model ->
            if ((model is KeywordItemViewModel) && model.isChecked) {
                selected.add(model)
            }
        }
        return selected
    }

    fun addNewKeyword(it: KeywordItemViewModel):Boolean {
        //  favoured.add(it)
        if (items.size != 0 && items[0] is KeywordEmptyViewModel) {
            items.clear()
            items.add(0, KeywordGroupViewModel("Kata Kunci Pilihan"))
            //  favoured.add(it)
        }
//        else if(items.size!=0 &&  items[0] is KeywordGroupViewModel &&(items[0] as KeywordGroupViewModel).title.equals("Kata Kunci Pilihan")){
//            favoured.add(it)
//        }
        else if (items.size != 0 && items[0] is KeywordGroupViewModel && (items[0] as KeywordGroupViewModel).title != "Kata Kunci Pilihan") {
            items.add(0, KeywordGroupViewModel("Kata Kunci Pilihan"))
            //   favoured.add(it)
        }

        favoured.forEachIndexed lit@{ index, keywordViewModel ->
                if (favoured[index].data.keyword == it.data.keyword) {
                    //if ((items[index] as KeywordItemViewModel).isChecked) {
                        return true
                  //  }
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
        notifyDataSetChanged()
        return false
    }









//        items.forEachIndexed { index, keywordViewModel ->
//            if(items[index] is KeywordItemViewModel){
//                if((items[index] as KeywordItemViewModel).data.keyword == it .data.keyword){
//                    if((items[index] as KeywordItemViewModel).isChecked){
//                        return true
//                    }
//                    ind = index
//                }
//            }
//        }
//        if (ind != 0)
//            items.removeAt(ind)
//
//        if (items.size != 0 && items[0] is KeywordEmptyViewModel) {
//            items.clear()
//            items.add(0, KeywordGroupViewModel("Kata Kunci Pilihan"))
//            items.add(1, it)
//        } else if (items.size != 0 && items[0] is KeywordGroupViewModel && (items.get(0) as KeywordGroupViewModel).title.equals("Kata Kunci Pilihan")) {
//            items.add(1, it)
//        } else {
//            items.add(0, KeywordGroupViewModel("Kata Kunci Pilihan"))
//            items.add(1, it)
//        }
//        (it as KeywordItemViewModel).isChecked = true
//
//        notifyDataSetChanged()
//        return false
 //   }

    fun addNewKeywords(it: List<KeywordItemViewModel>) {
        favoured.clear()
        remains.clear()
        favoured.addAll(it)
        items.removeAll(favoured)
        items.forEach { index ->
            if (index is KeywordItemViewModel)
                remains.add(index)
        }
        items.clear()
        items.add(0, KeywordGroupViewModel("Kata Kunci Pilihan"))
        items.addAll(favoured)
        items.forEach { index ->
            if (index is KeywordItemViewModel) {
                index.isChecked = true
            }
        }
        items.add(KeywordGroupViewModel("Rekomendasi"))
        items.addAll(remains)
        notifyDataSetChanged()
    }


//        remains.clear()
//        items.removeAll(it)
//        items.forEach { index ->
//            if (index is KeywordItemViewModel) {
//                remains.add(index)
//            }
//        }
//        items.clear()
//        items.add(0, KeywordGroupViewModel("Kata Kunci Pilihan"))
//        items.addAll(1, it)
//        items.forEach { index ->
//            if (index is KeywordItemViewModel) {
//                index.isChecked = true
//            }
//        }
//        items.add(KeywordGroupViewModel("Rekomendasi"))
//        Collections.sort(remains as List<KeywordItemViewModel>, kotlin.Comparator { o1: KeywordItemViewModel, o2: KeywordItemViewModel ->
//            when {
//                (o1.data.totalSearch > o2.data.totalSearch) -> 0
//
//                else -> 1
//            }
//        })
//        items.addAll(remains)
//        notifyDataSetChanged()
 //   }
}
