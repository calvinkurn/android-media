package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.data.model.datamodel.ProductShopInfoDataModel
import com.tokopedia.product.detail.view.fragment.partialview.PartialShopView
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import kotlinx.android.synthetic.main.partial_product_shop_info.view.*

class ProductShopInfoViewHolder(itemView: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductShopInfoDataModel>(itemView) {

    override fun bind(element: ProductShopInfoDataModel) {
        val shopInfoView = PartialShopView.build(itemView.base_shop_view, listener.onViewClickListener)
        if (element.shopInfo != null) {
            shopInfoView.renderShop(element.shopInfo ?: ShopInfo())
        }
    }
}