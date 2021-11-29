package com.tokopedia.buyerorder.detail.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.detail.view.adapter.uimodel.BuyerNormalProductUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class BuyerNormalProductViewHolder(itemView: View?): AbstractViewHolder<BuyerNormalProductUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.bottomsheet_cancel_product_item
    }

    private val productNameText: Typography? = itemView?.findViewById(R.id.tv_item_buyer_order_bundling_product_name)
    private val productPriceText: Typography? = itemView?.findViewById(R.id.tv_item_buyer_order_bundling_product_price)
    private val productThumbnailImage: ImageUnify? = itemView?.findViewById(R.id.iv_item_buyer_order_bundling_thumbnail)

    override fun bind(element: BuyerNormalProductUiModel) {
        productNameText?.text = element.productName
        productPriceText?.text = element.productPrice
        productThumbnailImage?.setImageUrl(element.productThumbnailUrl)
    }

}