package com.tokopedia.topads.edit.view.adapter.edit_keyword

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.edit.data.response.GetKeywordResponse
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewholder.EditKeywordViewHolder
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel.EditKeywordItemViewModel
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel.EditKeywordViewModel

/**
 * Created by Pika on 9/4/20.
 */

class EditKeywordListAdapter(val typeFactory: EditKeywordListAdapterTypeFactory) : RecyclerView.Adapter<EditKeywordViewHolder<EditKeywordViewModel>>() {


    var items: MutableList<EditKeywordViewModel> = mutableListOf()
    var data: MutableList<Int> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditKeywordViewHolder<EditKeywordViewModel> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.holder(viewType, view) as EditKeywordViewHolder<EditKeywordViewModel>
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type(typeFactory)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: EditKeywordViewHolder<EditKeywordViewModel>, position: Int) {
        holder.bind(items[position])
    }

    fun getCurrentItems(): List<GetKeywordResponse.KeywordsItem> {
        val selected: MutableList<GetKeywordResponse.KeywordsItem> = mutableListOf()
        items.forEach {
            if (it is EditKeywordItemViewModel) {
                selected.add(it.data)
            }
        }
        return selected
    }

    fun getBidData(list: MutableList<Int>) {
        this.data = list
        notifyDataSetChanged()
    }

}
