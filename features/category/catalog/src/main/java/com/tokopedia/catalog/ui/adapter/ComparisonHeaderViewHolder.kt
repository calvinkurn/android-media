package com.tokopedia.catalog.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.R
import com.tokopedia.catalog.databinding.ItemComparisonHeaderBinding
import com.tokopedia.utils.view.binding.viewBinding

class ComparisonHeaderViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun createRootView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comparison_header, parent, false)
    }

    private val binding: ItemComparisonHeaderBinding? by viewBinding()

    fun bind(itemData: String) {
        binding?.tfHeaderTitle?.text = itemData
    }
}
