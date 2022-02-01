package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.filter.common.data.Option
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductInspirationSizeOptionChipLayoutBinding
import com.tokopedia.search.result.presentation.model.InspirationSizeOptionDataView
import com.tokopedia.search.result.presentation.view.adapter.viewholder.InspirationSizeOptionAdapter
import com.tokopedia.search.result.presentation.view.listener.InspirationSizeOptionListener
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.view.binding.viewBinding

class InspirationSizeOptionViewHolder(
        itemView: View,
        private val inspirationOptionListener: InspirationSizeOptionListener,
        private val adapter: InspirationSizeOptionAdapter
): RecyclerView.ViewHolder(itemView) {
    companion object {
        val LAYOUT = R.layout.search_result_product_inspiration_size_option_chip_layout
    }

    private var binding: SearchResultProductInspirationSizeOptionChipLayoutBinding? by viewBinding()

    internal fun bind(optionData: InspirationSizeOptionDataView) {
        val binding = binding ?: return
        val chipsUnify =  binding.inspirationSizeOptionChip
        chipsUnify.chipText = optionData.text
        chipsUnify.chipType = if (optionData.isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL

        setListener(chipsUnify, optionData)
    }

    private fun setListener(chipsUnify: ChipsUnify, optionData: InspirationSizeOptionDataView) {
        chipsUnify.setOnClickListener {
            inspirationOptionListener.onInspirationSizeOptionClicked(optionData, createOption(optionData))
        }
    }

    private fun createOption(optionData: InspirationSizeOptionDataView) = Option(
        optionData.filters.name,
        optionData.filters.key,
        optionData.filters.value
    )
}