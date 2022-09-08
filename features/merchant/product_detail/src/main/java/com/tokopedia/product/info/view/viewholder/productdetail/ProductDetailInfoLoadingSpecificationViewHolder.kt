package com.tokopedia.product.info.view.viewholder.productdetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoLoadingSpecificationDataModel

/**
 * Created by Yehezkiel on 12/10/20
 */
class ProductDetailInfoLoadingSpecificationViewHolder(
    val view: View
) : AbstractViewHolder<ProductDetailInfoLoadingSpecificationDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.bs_item_product_detail_loading_specification
    }

    override fun bind(element: ProductDetailInfoLoadingSpecificationDataModel) {}
}