package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductInfoContent
import com.tokopedia.product.detail.common.data.model.product.Video
import com.tokopedia.product.detail.data.model.spesification.ProductSpecificationResponse
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

/**
 * This For "Deskripsi" and "Informasi Produk"
 */
data class ProductInfoDataModel(
        val type: String = "",
        val name: String = "",

        val data: List<ProductInfoContent>? = null,
        var videos: List<Video> = listOf()
) : DynamicPdpDataModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}