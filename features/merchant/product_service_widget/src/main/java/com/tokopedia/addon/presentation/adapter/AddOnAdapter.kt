package com.tokopedia.addon.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.addon.presentation.uimodel.AddOnGroupUIModel

class AddOnAdapter: RecyclerView.Adapter<AddOnViewHolder>() {

    private var items:List<AddOnGroupUIModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddOnViewHolder {
        val rootView = AddOnViewHolder.createRootView(parent)
        return AddOnViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: AddOnViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setItems(items: List<AddOnGroupUIModel>) {
        this.items = items
        notifyDataSetChanged()
    }

}
