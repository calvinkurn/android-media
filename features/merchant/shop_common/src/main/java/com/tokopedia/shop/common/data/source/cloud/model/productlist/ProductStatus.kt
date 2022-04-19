package com.tokopedia.shop.common.data.source.cloud.model.productlist

import androidx.annotation.Keep

@Keep
enum class ProductStatus {
    ACTIVE,
    INACTIVE,
    BANNED,
    EMPTY,
    MODERATED,
    DELETED,
    PENDING,
    VIOLATION
}