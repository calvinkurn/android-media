package com.tokopedia.buy_more_get_more.olp.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.databinding.ItemOlpOfferingProductBinding
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductListUiModel
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.widget.ProductListAdapter
import com.tokopedia.utils.view.binding.viewBinding

class OfferingProductListViewHolder(itemView: View) : AbstractViewHolder<OfferProductListUiModel>(itemView)  {

    private val binding: ItemOlpOfferingProductBinding? by viewBinding()
    private val productListAdapter by lazy { ProductListAdapter() }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_olp_offering_product
    }

    override fun bind(element: OfferProductListUiModel?) {
        productListAdapter.let { adapter ->
            element?.productList?.let { adapter.productList = it }
        }
        binding?.run {
            rvProductList.apply {
                layoutManager = StaggeredGridLayoutManager(
                    2,
                    StaggeredGridLayoutManager.VERTICAL
                )
                adapter = productListAdapter
            }
        }
    }
}
