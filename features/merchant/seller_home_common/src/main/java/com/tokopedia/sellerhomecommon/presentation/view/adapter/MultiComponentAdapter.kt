package com.tokopedia.sellerhomecommon.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentTab
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.multicomponent.MultiComponentTabViewHolder

class MultiComponentAdapter(private val items: MutableList<MultiComponentTab>) :
    RecyclerView.Adapter<MultiComponentTabViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiComponentTabViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.shc_multi_component_view, parent, false)
        return MultiComponentTabViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MultiComponentTabViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateTab(tab: MultiComponentTab) {
        val tabIndex = items.indexOfFirst { it.id == tab.id }
        items[tabIndex] = tab
        notifyItemChanged(tabIndex)
    }

}
