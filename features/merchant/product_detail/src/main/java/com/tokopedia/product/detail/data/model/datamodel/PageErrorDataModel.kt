package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class PageErrorDataModel(
        val type: String = "",
        val name: String = "",
        val errorCode: String = "",
        val errorMessage: String = "",
        val shouldShowTobacoError: Boolean = false,
        val tobacoErrorData: TobacoErrorData? = null
) : DynamicPDPDataModel {
    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = name
}

data class TobacoErrorData(
        val messages: String = "",
        val title: String = "",
        val button: String = "",
        val url: String = ""
)