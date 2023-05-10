package com.tokopedia.addon.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AddOnChildAdapter: RecyclerView.Adapter<AddOnChildViewHolder>() {

    private var items: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddOnChildViewHolder {
        val rootView = AddOnChildViewHolder.createRootView(parent)
        return AddOnChildViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: AddOnChildViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setItems(items: List<String>) {
        this.items = items
        notifyDataSetChanged()
    }

}
