package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductInfoContent
import com.tokopedia.product.detail.data.model.spesification.ProductSpecificationResponse
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

/**
 * This For "Deskripsi" and "Informasi Produk"
 */
data class ProductInfoDataModel(
        val data: List<ProductInfoContent>? = null,
        val type: String = "",
        val name: String = "",
        var shopName: String = "",
        var dynamicProductInfoP1: DynamicProductInfoP1? = null,
        var productSpecification: ProductSpecificationResponse? = null
) : DynamicPdpDataModel {
    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}