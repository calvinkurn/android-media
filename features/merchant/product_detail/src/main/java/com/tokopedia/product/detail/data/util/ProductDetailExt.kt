package com.tokopedia.product.detail.data.util

import com.tokopedia.product.detail.common.data.model.media.YoutubeVideo

const val KG = "kilogram"

val YoutubeVideo.thumbnailUrl: String
    get() = "https://img.youtube.com/vi/$url/1.jpg"
