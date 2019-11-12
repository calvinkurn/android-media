package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.product.detail.data.model.datamodel.ProductSocialProofDataModel
import com.tokopedia.product.detail.view.fragment.partialview.PartialAttributeInfoView
import kotlinx.android.synthetic.main.partial_product_detail_visibility.view.*

class ItemAttributeViewHolder(private val view: View) : BaseSocialProofViewHolder<ProductSocialProofDataModel>(view) {

    override fun bind(element: ProductSocialProofDataModel, position: Int) {
        val attributeInfoView = PartialAttributeInfoView.build(view.base_attribute)
        attributeInfoView.renderData(element.productInfo)
        attributeInfoView.renderWishlistCount(element.productInfoP2.wishlistCount.count)

    }

}