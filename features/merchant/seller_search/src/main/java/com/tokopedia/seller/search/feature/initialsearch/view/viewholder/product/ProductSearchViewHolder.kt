package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.SellerSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.ProductSearchListener
import kotlinx.android.synthetic.main.search_result_product.view.*

class ProductSearchViewHolder(view: View,
                            private val productSearchListener: ProductSearchListener): AbstractViewHolder<SellerSearchUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.search_result_product
    }

    private val adapterProduct by lazy { ItemProductSearchAdapter(productSearchListener) }

    override fun bind(element: SellerSearchUiModel) {
        with(itemView) {
            tvTitleResultProduct?.text = element.title
            rvResultProduct?.apply {
                layoutManager = LinearLayoutManager(itemView.context)
                adapter = adapterProduct
            }

            if(adapterPosition == element.count.orZero() - 1) {
                dividerProduct?.hide()
            }
        }

        if(element.sellerSearchList.isNotEmpty()) {
            adapterProduct.submitList(element.sellerSearchList)
        }
    }
}