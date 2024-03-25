package com.tokopedia.shop.product.view.datamodel

import com.tokopedia.shop.common.data.source.cloud.model.LabelGroupStyle

data class LabelGroupUiModel(
    val position: String = "",
    val type: String = "",
    val title: String = "",
    val url: String = "",
    val styles: List<LabelGroupStyle> = emptyList()
)
