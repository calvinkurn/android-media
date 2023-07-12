package com.tokopedia.promousage.view.model

data class PromoInfoUiModel(
    val methods: List<String> = emptyList(),
    val title: String = "",
    val iconUnify: String = "",
    val infoType: String = "",
    val validationType: String = ""
)
