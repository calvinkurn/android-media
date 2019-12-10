package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.common.data.model.pdplayout.ProductGeneralInfoData
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductGeneralInfoDataModel(
        val data: ProductGeneralInfoData? = null,
        val type: String = "",
        val name: String = "",
        var title: String = "",
        var applink: String = "",
        var description: String = "",
        var isApplink:Boolean = true
) : DynamicPDPDataModel {

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

}