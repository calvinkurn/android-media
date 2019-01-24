package com.tokopedia.product.detail.data.util

import com.tokopedia.product.detail.data.model.Campaign

const val MAX_PERCENT = 100

val Campaign.discountedPrice: Int
    get() = originalPrice * (MAX_PERCENT - percentage) / MAX_PERCENT