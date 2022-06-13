package com.tokopedia.search.result.product.inspirationwidget.size

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductInspirationSizeOptionChipLayoutBinding
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.view.binding.viewBinding

class InspirationSizeOptionViewHolder(
    itemView: View,
    private val inspirationSizeListener: InspirationSizeListener,
): RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.search_result_product_inspiration_size_option_chip_layout
    }

    private var binding: SearchResultProductInspirationSizeOptionChipLayoutBinding? by viewBinding()

    internal fun bind(optionData: InspirationSizeOptionDataView) {
        val binding = binding ?: return
        val chipsUnify =  binding.inspirationSizeOptionChip

        chipsUnify.chipText = optionData.text
        chipsUnify.chipType = getChipType(optionData)
        chipsUnify.setOnClickListener {
            inspirationSizeListener.onInspirationSizeOptionClicked(optionData)
        }
    }

    private fun getChipType(optionData: InspirationSizeOptionDataView) =
        if (inspirationSizeListener.isFilterSelected(optionData.option))
            ChipsUnify.TYPE_SELECTED
        else
            ChipsUnify.TYPE_NORMAL
}