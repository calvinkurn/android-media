package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.common.data.model.pdplayout.Content
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductGeneralInfoDataModel(
        val name: String = "",
        val type: String = "",
        val applink: String = "",
        val title: String = "",
        val isApplink: Boolean = false,
        val parentIcon: String = "",
        val data: List<Content> = listOf(Content())
) : DynamicPDPDataModel {

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

}