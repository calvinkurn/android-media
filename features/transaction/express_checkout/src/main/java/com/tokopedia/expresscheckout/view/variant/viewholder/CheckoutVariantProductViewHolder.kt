package com.tokopedia.expresscheckout.view.variant.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.view.variant.viewmodel.CheckoutVariantProductViewModel
import com.tokopedia.expresscheckout.view.variant.viewmodel.ProductChild
import kotlinx.android.synthetic.main.item_product_detail_product_page.view.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantProductViewHolder(val view: View) : AbstractViewHolder<CheckoutVariantProductViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_product_detail_product_page
    }

    override fun bind(element: CheckoutVariantProductViewModel?) {
        if (element != null) {
            ImageHandler.loadImageRounded2(itemView.context, itemView.img_product, element.productImageUrl)
            if (element.productChildrenList.isNotEmpty()) {
                for (productChild: ProductChild in element.productChildrenList) {
                    if (productChild.isSelected) {
                        itemView.tv_product_name.text = productChild.productName
                        itemView.tv_product_price.text = productChild.productPrice
                    }
                }
            } else {
                itemView.tv_product_name.text = element.productName
                itemView.tv_product_price.text = element.productPrice
            }
        }
    }

}