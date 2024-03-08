package com.tokopedia.catalogcommon.viewholder.comparison

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalogcommon.uimodel.ComparisonUiModel

class ComparisonSpecItemAdapter(
    private val items: List<ComparisonUiModel.ComparisonSpec> = listOf(),
    private val isComparedItem: Boolean = false
) : RecyclerView.Adapter<ComparisonSpecItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComparisonSpecItemViewHolder {
        val rootView = ComparisonSpecItemViewHolder.createRootView(parent)
        return ComparisonSpecItemViewHolder(rootView, isComparedItem)
    }

    override fun onBindViewHolder(holder: ComparisonSpecItemViewHolder, position: Int) {
        holder.bind(items[position], position < itemCount.dec())
    }

    override fun getItemCount() = items.size
}
