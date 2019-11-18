package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.ComponentData
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.data.model.ProductInfoP2General
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductSocialProofDataModel(
        val type: String = "",
        val name: String = "",
        val dataLayout: List<ComponentData> = listOf(),
        //P1
        var productInfo: ProductInfo = ProductInfo(),
        //P2
        var productInfoP2: ProductInfoP2General = ProductInfoP2General()
) : DynamicPDPDataModel {
    override fun name(): String = name

    companion object {
        val LAYOUT = R.layout.item_dynamic_pdp_social_proof
    }

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}