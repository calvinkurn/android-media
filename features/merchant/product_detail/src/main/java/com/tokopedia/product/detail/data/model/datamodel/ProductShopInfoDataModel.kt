package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.ComponentData
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo

class ProductShopInfoDataModel(
        val dataLayout: List<ComponentData> = listOf(),
        val type:String = "",
        var shopInfo: ShopInfo? = null

) : DynamicPDPDataModel {
    companion object {
        val LAYOUT = R.layout.item_dynamic_pdp_shop_info
    }

    override fun type(): String = type


    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}