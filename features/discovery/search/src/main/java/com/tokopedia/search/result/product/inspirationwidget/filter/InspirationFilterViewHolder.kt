package com.tokopedia.search.result.product.inspirationwidget.filter

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductSizeLayoutBinding
import com.tokopedia.search.utils.ChipSpacingItemDecoration
import com.tokopedia.search.utils.addItemDecorationIfNotExists
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

internal class InspirationFilterViewHolder(
    itemView: View,
    private val inspirationFilterListener: InspirationFilterListener
): AbstractViewHolder<InspirationFilterDataView>(itemView) {
    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_result_product_size_layout
    }

    private var binding: SearchResultProductSizeLayoutBinding? by viewBinding()
    private var inspirationFilterOptionAdapter: InspirationFilterOptionAdapter? = null

    override fun bind(element: InspirationFilterDataView) {
        bindHeader(element)
        bindTitle(element)
        bindOptions(element)
    }

    private fun bindHeader(element: InspirationFilterDataView) {
        binding?.inspirationWidgetHeaderTitle?.run {
            showWithCondition(element.data.headerTitle.isNotEmpty())
            text = element.data.headerTitle
        }

        binding?.inspirationWidgetHeaderSubtitle?.run {
            showWithCondition(element.data.headerSubtitle.isNotEmpty())
            text = element.data.headerSubtitle
        }
    }

    private fun bindTitle(element: InspirationFilterDataView) {
        binding?.searchProductSizeTitle?.text = element.data.title
    }

    private fun bindOptions(element: InspirationFilterDataView) {
        val chipVerticalSpacing = 4.toPx()
        binding?.searchProductSizeOptionRecyclerView?.apply {
            layoutManager = createLayoutManager()
            createAdapter(element)
            adapter = inspirationFilterOptionAdapter
            addItemDecorationIfNotExists(ChipSpacingItemDecoration(chipVerticalSpacing, 0))
        }
    }

    private fun createLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun createAdapter(element: InspirationFilterDataView) {
        val adapter = InspirationFilterOptionAdapter(inspirationFilterListener)
        val sortedSizeData = getSortedSizeData(element.optionSizeData)
        adapter.setItemList(sortedSizeData)

        inspirationFilterOptionAdapter = adapter
    }

    private fun getSortedSizeData(
        optionSizeData: List<InspirationFilterOptionDataView>
    ) : List<InspirationFilterOptionDataView> {
        val sortedSelectedSizeData = optionSizeData
            .filter { inspirationFilterListener.isFilterSelected(it.optionList) }
            .sortedByOptionValue()
        val nonSelectedSizeData = optionSizeData - sortedSelectedSizeData.toSet()
        val sortedNonSelectedSizeData = nonSelectedSizeData.sortedByOptionValue()
        return sortedSelectedSizeData + sortedNonSelectedSizeData
    }

    private fun List<InspirationFilterOptionDataView>.sortedByOptionValue() : List<InspirationFilterOptionDataView> {
        return sortedBy {
            try {
                it.optionList.minOf { option -> option.value.toIntOrZero() }
            } catch (e: Throwable) {
                0
            }
        }
    }
}
