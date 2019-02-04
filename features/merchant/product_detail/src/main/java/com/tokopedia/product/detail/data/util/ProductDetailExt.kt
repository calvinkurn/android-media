package com.tokopedia.product.detail.data.util

import com.tokopedia.product.detail.data.model.product.Campaign
import com.tokopedia.product.detail.data.model.product.Video

const val MAX_PERCENT = 100

val Campaign.discountedPrice: Int
    get() = originalPrice * (MAX_PERCENT - percentage) / MAX_PERCENT

val Video.thumbnailUrl: String
    get() = "http://img.youtube.com/vi/$url/1.jpg"