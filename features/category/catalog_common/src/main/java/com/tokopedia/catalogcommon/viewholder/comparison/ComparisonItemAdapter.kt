package com.tokopedia.catalogcommon.viewholder.comparison

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalogcommon.uimodel.ComparisonUiModel
import com.tokopedia.catalogcommon.viewholder.ComparisonViewHolder

class ComparisonItemAdapter(
    private val items: List<ComparisonUiModel.ComparisonContent> = listOf(),
    private val itemWidth: Int,
    private val isDisplayingTopSpec: Boolean = false,
    private val comparisonItemListener: ComparisonViewHolder.ComparisonItemListener? = null
) : RecyclerView.Adapter<ComparisonItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComparisonItemViewHolder {
        val rootView = ComparisonItemViewHolder.createRootView(parent)
        return ComparisonItemViewHolder(rootView, isDisplayingTopSpec, comparisonItemListener)
    }

    override fun onBindViewHolder(holder: ComparisonItemViewHolder, position: Int) {
        holder.bind(items[position], itemWidth, position == itemCount.dec())
    }

    override fun getItemCount() = items.size
}
