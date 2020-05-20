package com.tokopedia.topads.edit.view.adapter.edit_neg_keyword

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.edit.data.param.NegKeyword
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewholder.EditNegKeywordViewHolder
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewmodel.EditNegKeywordItemViewModel
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewmodel.EditNegKeywordViewModel

/**
 * Created by Pika on 12/4/20.
 */

class EditNegKeywordListAdapter(val typeFactory: EditNegKeywordListAdapterTypeFactory) : RecyclerView.Adapter<EditNegKeywordViewHolder<EditNegKeywordViewModel>>() {


    var items: MutableList<EditNegKeywordViewModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditNegKeywordViewHolder<EditNegKeywordViewModel> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.holder(viewType, view) as EditNegKeywordViewHolder<EditNegKeywordViewModel>
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type(typeFactory)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: EditNegKeywordViewHolder<EditNegKeywordViewModel>, position: Int) {
        holder.bind(items[position])
    }

    fun getCurrentItems(): List<NegKeyword> {
        val list: ArrayList<NegKeyword> = arrayListOf()
        items.forEach {
            if (it is EditNegKeywordItemViewModel)
                list.add(it.data)
        }
        return list
    }
}
