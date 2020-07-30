package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

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
        var shouldRenderShopInfo: Boolean = false,

        //From P2 General
        var shopBadge: String? = "",
        var isGoAPotik:Boolean = false,

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