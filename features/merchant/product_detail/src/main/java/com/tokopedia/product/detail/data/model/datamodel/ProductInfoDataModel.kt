package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.common.data.model.pdplayout.ComponentData
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.data.model.spesification.ProductSpecificationResponse
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo

/**
 * This For "Deskripsi" and "Informasi Produk"
 */
data class ProductInfoDataModel(
        val dataLayout:List<ComponentData> = listOf(),
        val type: String = "",
        var productInfo: ProductInfo? = null,
        var shopInfo: ShopInfo? = null,
        var productSpecification: ProductSpecificationResponse? = null
) : DynamicPDPDataModel {

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}