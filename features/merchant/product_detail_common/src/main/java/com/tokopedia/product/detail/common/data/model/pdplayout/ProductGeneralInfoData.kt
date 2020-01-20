package com.tokopedia.product.detail.common.data.model.pdplayout

data class ProductGeneralInfoData(
        val applink: String = "",
        val title: String = "",
        val isApplink: Boolean = false,
        val parentIcon: String = "",
        val listOfContent: List<Content> = listOf()
)