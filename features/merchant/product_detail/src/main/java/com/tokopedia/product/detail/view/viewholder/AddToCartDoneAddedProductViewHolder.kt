package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneAddedProductViewModel

class AddToCartDoneAddedProductViewHolder(
        itemView: View
) : AbstractViewHolder<AddToCartDoneAddedProductViewModel>(itemView) {
    override fun bind(element: AddToCartDoneAddedProductViewModel?) {

    }

    companion object {
        val LAYOUT_RES = R.layout.add_to_cart_done_added_product_layout
    }
}