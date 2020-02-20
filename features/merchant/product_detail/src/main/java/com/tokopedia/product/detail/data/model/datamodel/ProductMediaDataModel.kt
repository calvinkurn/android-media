package com.tokopedia.product.detail.data.model.datamodel

data class ProductMediaDataModel(
        val type: String = "",
        val url300: String = "",
        val urlOriginal: String = "",
        val urlThumbnail: String = "",
        val mediaDescription: String = "",
        val videoUrl: String = "",
        val isAutoPlay: Boolean = false
)