package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.product.Media
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductSnapshotDataModel(
        var media: List<Media> = listOf(),
        var productInfoP1: ProductInfo = ProductInfo()
) : DynamicPDPDataModel {
    companion object {
        val LAYOUT = R.layout.item_dynamic_pdp_snapshot
    }

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int = typeFactory.type(this)
}