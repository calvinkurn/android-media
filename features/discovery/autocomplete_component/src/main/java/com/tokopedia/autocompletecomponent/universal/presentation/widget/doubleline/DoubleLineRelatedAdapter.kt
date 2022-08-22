package com.tokopedia.autocompletecomponent.universal.presentation.widget.doubleline

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.universal.presentation.model.RelatedItemDataView

internal class DoubleLineRelatedAdapter: RecyclerView.Adapter<DoubleLineRelatedViewHolder>() {

    private val list = ArrayList<RelatedItemDataView>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoubleLineRelatedViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(DoubleLineRelatedViewHolder.LAYOUT, parent, false)

        return DoubleLineRelatedViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoubleLineRelatedViewHolder, position: Int) {
        holder.bind(list[position])
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