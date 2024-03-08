package com.tokopedia.catalog.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ComparisonHeaderAdapter(
    private val items: List<String> = emptyList()
) : RecyclerView.Adapter<ComparisonHeaderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComparisonHeaderViewHolder {
        val rootView = ComparisonHeaderViewHolder.createRootView(parent)
        return ComparisonHeaderViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: ComparisonHeaderViewHolder, position: Int) {
        holder.bind(items.getOrNull(position) ?: return)
    }

    override fun getItemCount() = items.size
}
