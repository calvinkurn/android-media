package com.tokopedia.filter.bottomsheet.pricerangecheckbox

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.filter.R
import com.tokopedia.filter.bottomsheet.pricerangecheckbox.item.PriceRangeFilterCheckboxItemAdapter
import com.tokopedia.filter.bottomsheet.pricerangecheckbox.item.PriceRangeFilterCheckboxListener
import com.tokopedia.filter.databinding.FilterPriceRangeLayoutBinding

internal class PriceRangeFilterCheckboxViewHolder(
    itemView: View,
    private val priceRangeFilterCheckboxListener: PriceRangeFilterCheckboxListener
) : AbstractViewHolder<PriceRangeFilterCheckboxUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.filter_price_range_layout

        const val STRING_COLOR_INDEX = 2
    }

    private val binding = FilterPriceRangeLayoutBinding.bind(itemView)

    init {
        binding.rvPriceRange.run {
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
        }
    }

    override fun bind(element: PriceRangeFilterCheckboxUiModel) {
        bindRvFilterPriceRange(element)
        binding.tvPriceRangeLabel.text = element.priceRangeLabel
    }

    private fun bindRvFilterPriceRange(priceRangeFilterCheckboxUiModel: PriceRangeFilterCheckboxUiModel) {
        val priceRangeOptionAdapter =
            PriceRangeFilterCheckboxItemAdapter(
                priceRangeFilterCheckboxUiModel.priceRangeList,
                priceRangeFilterCheckboxListener
            )
        val removeAndRecycleExistingViews = false
        binding.rvPriceRange.swapAdapter(priceRangeOptionAdapter, removeAndRecycleExistingViews)
    }

}