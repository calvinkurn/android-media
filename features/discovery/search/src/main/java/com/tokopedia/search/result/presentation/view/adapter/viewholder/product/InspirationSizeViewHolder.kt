package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductSizeLayoutBinding
import com.tokopedia.search.result.presentation.model.SizeDataView
import com.tokopedia.search.result.presentation.model.SizeOptionDataView
import com.tokopedia.search.result.presentation.view.adapter.viewholder.InspirationSizeOptionAdapter
import com.tokopedia.search.result.presentation.view.listener.InspirationSizeOptionListener
import com.tokopedia.utils.view.binding.viewBinding

internal class InspirationSizeViewHolder(
        itemView: View,
        private val inspirationSizeOptionListener: InspirationSizeOptionListener
): AbstractViewHolder<SizeDataView>(itemView) {
    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_result_product_size_layout
    }

    private var binding: SearchResultProductSizeLayoutBinding? by viewBinding()

    override fun bind(element: SizeDataView) {
        bindTitle(element)
        bindOptions(element)
    }

    private fun bindTitle(element: SizeDataView) {
        binding?.searchProductSizeTitle?.text = element.title
    }

    private fun bindOptions(element: SizeDataView) {
        binding?.searchProductSizeOptionRecyclerView?.apply {
            layoutManager = createLayoutManager()
            adapter = createAdapter(element.optionData)
        }
    }

    private fun createLayoutManager(): ChipsLayoutManager {
        return ChipsLayoutManager.newBuilder(itemView.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
    }

    private fun createAdapter(inspirationSizeOptionData: List<SizeOptionDataView>): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        val inspirationSizeOptionAdapter = InspirationSizeOptionAdapter(inspirationSizeOptionListener)
        inspirationSizeOptionAdapter.setItemList(inspirationSizeOptionData)

        return inspirationSizeOptionAdapter
    }
}