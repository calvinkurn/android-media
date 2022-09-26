package com.tokopedia.autocompletecomponent.universal.presentation.widget.related

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RelatedAdapter(
    private val relatedItemListener: RelatedItemListener,
    private val type: Int,
): RecyclerView.Adapter<RelatedItemViewHolder>() {

    private val list = ArrayList<RelatedItemDataView>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedItemViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(RelatedItemViewHolder.LAYOUT, parent, false)

        return RelatedItemViewHolder(view, relatedItemListener)
    }

    override fun onBindViewHolder(holder: RelatedItemViewHolder, position: Int) {
        holder.bind(list[position], type)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(newList: List<RelatedItemDataView>) {
        list.clear()
        list.addAll(newList)

        notifyItemRangeInserted(0, newList.size)
    }
}