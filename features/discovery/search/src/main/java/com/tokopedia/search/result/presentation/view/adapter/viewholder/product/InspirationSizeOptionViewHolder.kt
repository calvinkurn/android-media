package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductInspirationSizeOptionChipLayoutBinding
import com.tokopedia.search.result.presentation.model.InspirationCardOptionDataView
import com.tokopedia.search.result.presentation.model.SizeOptionDataView
import com.tokopedia.search.result.presentation.view.listener.InspirationSizeOptionListener
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import com.tokopedia.utils.view.binding.viewBinding

class InspirationSizeOptionViewHolder(
        itemView: View,
        private val inspirationOptionListener: InspirationSizeOptionListener
): RecyclerView.ViewHolder(itemView) {
    companion object {
        val LAYOUT = R.layout.search_result_product_inspiration_size_option_chip_layout
    }

    private var binding: SearchResultProductInspirationSizeOptionChipLayoutBinding? by viewBinding()

    internal fun bind(optionData: SizeOptionDataView) {
        binding?.inspirationSizeOptionChip?.chipText = optionData.text

        setListener(optionData)
    }

    private fun setListener(optionData: SizeOptionDataView) {
        inspirationOptionListener.onInspirationSizeOptionClicked(optionData)
    }
}