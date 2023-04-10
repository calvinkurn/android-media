package com.tokopedia.search.result.product.inspirationwidget.filter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductInspirationFilterOptionChipLayoutBinding
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.view.binding.viewBinding

class InspirationFilterOptionViewHolder(
    itemView: View,
    private val inspirationFilterListener: InspirationFilterListener,
    private val inspirationFilterOptionListener: InspirationFilterOptionListener,
): RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.search_result_product_inspiration_filter_option_chip_layout
    }

    private var binding: SearchResultProductInspirationFilterOptionChipLayoutBinding? by viewBinding()

    internal fun bind(optionData: InspirationFilterOptionDataView) {
        val binding = binding ?: return
        val chipsUnify =  binding.inspirationFilterOptionChip

        chipsUnify.chipText = optionData.text
        chipsUnify.chipType = getChipType(optionData)
        chipsUnify.setOnClickListener {
            inspirationFilterOptionListener.onInspirationFilterOptionClicked(optionData)
        }
    }

    private fun getChipType(optionData: InspirationFilterOptionDataView) =
        if (inspirationFilterListener.isFilterSelected(optionData.optionList))
            ChipsUnify.TYPE_SELECTED
        else
            ChipsUnify.TYPE_NORMAL
}
