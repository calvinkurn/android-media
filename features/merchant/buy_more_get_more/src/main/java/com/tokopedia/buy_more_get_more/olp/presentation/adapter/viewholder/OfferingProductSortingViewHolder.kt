package com.tokopedia.buy_more_get_more.olp.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.databinding.ItemOlpSortingBinding
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductSortingUiModel
import com.tokopedia.buy_more_get_more.sort.listener.ProductSortListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.utils.view.binding.viewBinding

class OfferingProductSortingViewHolder(
    view: View,
    private val sortListener: ProductSortListener
) :
    AbstractViewHolder<OfferProductSortingUiModel>(view) {

    private val binding: ItemOlpSortingBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_olp_sorting
    }

    override fun bind(data: OfferProductSortingUiModel) {
        binding?.apply {
            tpgProductCount.apply {
                text = getString(R.string.bmgm_product_count_placeholder, data.productCount.toString())
                showWithCondition(data.isProductCountVisible)
            }
            chipsSorting.apply {
                val filter = SortFilterItem(data.selectedSortName)
                addItem(arrayListOf(filter))
                filter.refChipUnify.apply {
                    setChevronClickListener {
                        sortListener.onSortChipClicked()
                    }
                    setOnClickListener {
                        sortListener.onSortChipClicked()
                    }
                }
            }
        }
    }
}
