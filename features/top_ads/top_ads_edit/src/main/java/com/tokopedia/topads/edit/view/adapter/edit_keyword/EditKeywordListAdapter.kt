package com.tokopedia.topads.edit.view.adapter.edit_keyword

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.common.data.response.GetKeywordResponse
import com.tokopedia.topads.edit.data.KeySharedModel
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewholder.EditKeywordViewHolder
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel.EditKeywordItemViewModel
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel.EditKeywordViewModel

/**
 * Created by Pika on 9/4/20.
 */

class EditKeywordListAdapter(val typeFactory: EditKeywordListAdapterTypeFactory) : RecyclerView.Adapter<EditKeywordViewHolder<EditKeywordViewModel>>() {


    var items: MutableList<EditKeywordViewModel> = mutableListOf()
    var data: MutableList<String> = mutableListOf()
    var added: MutableList<Boolean> = mutableListOf()
    var minBid: Float = 0.0f


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
        holder.bind(items[position],added,minBid)
    }

    fun getCurrentItems(): List<KeySharedModel> {
        val selected: MutableList<KeySharedModel> = mutableListOf()
        items.forEach {
            if (it is EditKeywordItemViewModel) {
                selected.add(it.data)
            }
        }
        return selected
    }

    fun getBidData(list: MutableList<String>, isnewlyAddded: MutableList<Boolean>) {
        this.data = list
        this.added = isnewlyAddded
        notifyDataSetChanged()
    }

    fun clearList(){
        this.items.clear()
        this.added.clear()
        this.data.clear()
    }

    fun setBid(bid: Float) {
        minBid = bid
        notifyDataSetChanged()
    }
}
