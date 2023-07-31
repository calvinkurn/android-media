package com.tokopedia.buy_more_get_more.olp.presentation.adapter

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.databinding.ItemOlpSortingBinding
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductSortingUiModel
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.utils.view.binding.viewBinding

class OfferingProductSortingViewHolder(view: View) :
    AbstractViewHolder<OfferProductSortingUiModel>(view) {

    private val binding: ItemOlpSortingBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_olp_sorting
    }

    override fun bind(data: OfferProductSortingUiModel) {
        binding?.apply {
            tpgProductCount?.text =
                getString(R.string.bmgm_product_count_placeholder, data.productCount.toString())
            chipsSorting.apply {
                val filter = SortFilterItem("Urutkan")
                addItem(arrayListOf(filter))
            }
        }
    }
}
