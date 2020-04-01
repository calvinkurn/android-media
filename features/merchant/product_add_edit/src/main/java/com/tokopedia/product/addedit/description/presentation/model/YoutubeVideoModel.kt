package com.tokopedia.product.addedit.description.presentation.model

data class YoutubeVideoModel(
        val snippetTitle: String? = "",
        val snippetDescription: String? = "",
        val thumbnailUrl: String? = "",
        val height: Int? = 0,
        val width: Int? = 0
)