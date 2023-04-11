package com.tokopedia.product.info.view.viewholder.productdetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.info.view.models.ProductDetailInfoLoadingDescriptionDataModel

/**
 * Created by Yehezkiel on 12/10/20
 */
class ProductDetailInfoLoadingDescriptionViewHolder(
    val view: View
) : AbstractViewHolder<ProductDetailInfoLoadingDescriptionDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.bs_item_product_detail_loading_description
    }

    override fun bind(element: ProductDetailInfoLoadingDescriptionDataModel) {}
}