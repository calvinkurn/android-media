package com.tokopedia.product.estimasiongkir.view.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingShimmerDataModel

/**
 * Created by Yehezkiel on 08/02/21
 */
class ProductShippingShimmerViewHolder(view: View) : AbstractViewHolder<ProductShippingShimmerDataModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_product_shipping_shimmer
    }

    override fun bind(element: ProductShippingShimmerDataModel) {
        if (element.height == 0) {
            itemView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } else {
            itemView.layoutParams.height = element.height
        }
    }
}