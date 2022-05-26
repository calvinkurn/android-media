package com.tokopedia.shop.flash_sale.presentation.draft.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.flash_sale.presentation.draft.uimodel.DraftItemModel

class DraftListAdapter: RecyclerView.Adapter<DraftListViewHolder>() {

    private var items: List<DraftItemModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DraftListViewHolder {
        val rootView = DraftListViewHolder.createRootView(parent)
        return DraftListViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: DraftListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setItems(items: List<DraftItemModel>) {
        this.items = items
    }
}