package com.tokopedia.topads.edit.view.adapter.keyword

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.edit.view.adapter.keyword.viewholder.KeywordViewHolder
import com.tokopedia.topads.edit.view.adapter.keyword.viewmodel.KeywordViewModel
import com.tokopedia.topads.edit.view.adapter.keyword.viewmodel.KeywordItemViewModel

class KeywordListAdapter(private val typeFactory: KeywordListAdapterTypeFactory) : RecyclerView.Adapter<KeywordViewHolder<KeywordViewModel>>() {


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

    override fun onBindViewHolder(holder: KeywordViewHolder<KeywordViewModel>, position: Int) {
        holder.bind(items[position])
    }

    fun getSelectedItems(): List<KeywordItemViewModel> {
        val selected = mutableListOf<KeywordItemViewModel>()
        items.forEachIndexed { _, model ->
            if ((model is KeywordItemViewModel) && model.isChecked) {
                selected.add(model)
            }
        }
        return selected
    }


    companion object {
        fun createInstance(typeFactory: KeywordListAdapterTypeFactory): KeywordListAdapter {
            return KeywordListAdapter(typeFactory)
        }
    }
}
