package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.model.shopfeature.ShopFeatureData
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo

class ProductShopInfoDataModel(
        //From P1
        val type: String = "",
        val name: String = "",
        var isOs: Boolean = false,
        var isPm: Boolean = false,

        //From P2 Shop
        var isFavorite: Boolean = false,
        var shopAvatar: String = "",
        var isAllowManage: Int = 0,
        var shopName: String = "",
        var shopLocation: String = "",
        var shopLastActive: String = "",

        //From P2 General
        var shopBadge: ShopBadge? = null,
        var shopFeature: ShopFeatureData? = null,

        //Static
        var enableButtonFavorite: Boolean = false

) : DynamicPdpDataModel {
    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}