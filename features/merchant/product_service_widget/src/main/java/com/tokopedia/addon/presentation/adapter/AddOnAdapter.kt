package com.tokopedia.addon.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AddOnAdapter: RecyclerView.Adapter<AddOnViewHolder>() {

    private var items: List<Pair<String, List<String>>> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddOnViewHolder {
        val rootView = AddOnViewHolder.createRootView(parent)
        return AddOnViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: AddOnViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setItems(items: List<Pair<String, List<String>>>) {
        this.items = items
        notifyDataSetChanged()
    }

}
