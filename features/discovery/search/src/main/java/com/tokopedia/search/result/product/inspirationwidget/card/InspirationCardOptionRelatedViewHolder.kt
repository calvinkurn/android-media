package com.tokopedia.search.result.product.inspirationwidget.card

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductInspirationCardOptionRelatedLayoutBinding
import com.tokopedia.search.result.product.inspirationwidget.card.InspirationCardListener
import com.tokopedia.search.result.product.inspirationwidget.card.InspirationCardOptionDataView
import com.tokopedia.utils.view.binding.viewBinding

class InspirationCardOptionRelatedViewHolder(
        itemView: View,
        private val inspirationCardListener: InspirationCardListener
): RecyclerView.ViewHolder(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_result_product_inspiration_card_option_related_layout
    }

    private var binding: SearchResultProductInspirationCardOptionRelatedLayoutBinding? by viewBinding()

    fun bind(inspirationCardOptionDataView: InspirationCardOptionDataView, spanCount: Int) {
        val binding = binding ?: return

        binding.inspirationCardRelatedImage.urlSrc = inspirationCardOptionDataView.img
        binding.inspirationCardRelatedTitle.text = inspirationCardOptionDataView.text

        val isTopOfRecyclerView = adapterPosition < spanCount
        binding.inspirationCardSeparator.showWithCondition(!isTopOfRecyclerView)

        binding.inspirationCardRelatedContainer.setOnClickListener {
            inspirationCardListener.onInspirationCardOptionClicked(inspirationCardOptionDataView)
        }
    }
}