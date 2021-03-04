package com.tokopedia.product.info.view.viewholder.productdetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoLoadingDataModel

/**
 * Created by Yehezkiel on 12/10/20
 */
class ProductDetailInfoLoadingViewHolder(val view: View) : AbstractViewHolder<ProductDetailInfoLoadingDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.bs_item_product_detail_loading
    }

    override fun bind(element: ProductDetailInfoLoadingDataModel) {}
}