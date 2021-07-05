package com.tokopedia.product.detail.data.util

import com.tokopedia.product.detail.common.data.model.product.YoutubeVideo

const val KG = "kilogram"

const val LABEL_KG = "Kg"
const val LABEL_GRAM = "gram"

val YoutubeVideo.thumbnailUrl: String
    get() = "https://img.youtube.com/vi/$url/1.jpg"