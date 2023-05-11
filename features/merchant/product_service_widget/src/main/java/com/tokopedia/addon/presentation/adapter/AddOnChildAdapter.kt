package com.tokopedia.addon.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.addon.presentation.uimodel.AddOnUIModel

class AddOnChildAdapter: RecyclerView.Adapter<AddOnChildViewHolder>() {

    private var items: List<AddOnUIModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddOnChildViewHolder {
        val rootView = AddOnChildViewHolder.createRootView(parent)
        return AddOnChildViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: AddOnChildViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setItems(items: List<AddOnUIModel>) {
        this.items = items
        notifyDataSetChanged()
    }

}
