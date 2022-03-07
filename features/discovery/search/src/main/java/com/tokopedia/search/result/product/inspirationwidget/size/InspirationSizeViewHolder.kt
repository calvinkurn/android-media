package com.tokopedia.search.result.product.inspirationwidget.size

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductSizeLayoutBinding
import com.tokopedia.search.utils.ChipSpacingItemDecoration
import com.tokopedia.search.utils.addItemDecorationIfNotExists
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

internal class InspirationSizeViewHolder(
        itemView: View,
        private val inspirationSizeListener: InspirationSizeListener
): AbstractViewHolder<InspirationSizeDataView>(itemView) {
    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_result_product_size_layout
    }

    private var binding: SearchResultProductSizeLayoutBinding? by viewBinding()
    private var inspirationSizeOptionAdapter: InspirationSizeOptionAdapter? = null

    override fun bind(element: InspirationSizeDataView) {
        bindTitle(element)
        bindOptions(element)
    }

    private fun bindTitle(element: InspirationSizeDataView) {
        binding?.searchProductSizeTitle?.text = element.data.title
    }

    private fun bindOptions(element: InspirationSizeDataView) {
        val chipVerticalSpacaing = 4.toPx()
        binding?.searchProductSizeOptionRecyclerView?.apply {
            layoutManager = createLayoutManager()
            createAdapter(element)
            adapter = inspirationSizeOptionAdapter
            addItemDecorationIfNotExists(ChipSpacingItemDecoration(chipVerticalSpacaing, 0))
        }
    }

    private fun createLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun createAdapter(element: InspirationSizeDataView) {
        val adapter = InspirationSizeOptionAdapter(inspirationSizeListener)
        adapter.setItemList(element.optionSizeData)
        adapter.setInspirationSizeDataView(element)

        inspirationSizeOptionAdapter = adapter
    }
}