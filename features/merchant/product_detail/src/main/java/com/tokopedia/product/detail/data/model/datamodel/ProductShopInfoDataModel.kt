package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.data.model.shopfeature.ShopFeatureData
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo

class ProductShopInfoDataModel(
        val type: String = "",
        val name: String = "",
        var isFavorite: Boolean = false,
        var toogleFavorite: Boolean = false,
        var shopInfo: ShopInfo? = null,
        var shopBadge: ShopBadge? = null,
        var shopFeature: ShopFeatureData? = null

) : DynamicPdpDataModel {
    override fun name(): String = name

    override fun type(): String = type


    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}