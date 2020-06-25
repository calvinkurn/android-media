package com.tokopedia.seller.search.feature.suggestion.view.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.PRODUCT
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.ProductSearchListener
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.SellerSearchUiModel
import kotlinx.android.synthetic.main.search_result_product.view.*

class ProductSearchViewHolder(private val view: View,
                              private val productSearchListener: ProductSearchListener) : AbstractViewHolder<SellerSearchUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.search_result_product
    }

    private val adapterProduct by lazy { ItemProductSearchAdapter(productSearchListener) }

    override fun bind(element: SellerSearchUiModel) {
        view.apply {
            tvTitleResultProduct?.text = element.title
            rvResultProduct?.apply {
                layoutManager = LinearLayoutManager(view.context)
                adapter = adapterProduct
            }
        }

        if (adapterPosition == element.count.orZero() - 1) {
            view.dividerProduct?.hide()
        }

        if (element.sellerSearchList.isNotEmpty()) {
            adapterProduct.clearAllData()
            element.takeIf { it.id == PRODUCT }?.sellerSearchList?.let {
                adapterProduct.setItemProductList(it)
            }
        }
    }
}