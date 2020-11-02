package com.tokopedia.shop.common.data.source.cloud.model.productlist

import com.google.gson.annotations.SerializedName

enum class ProductStatus {
    @SerializedName("ACTIVE")
    ACTIVE,
    @SerializedName("INACTIVE")
    INACTIVE,
    @SerializedName("BANNED")
    BANNED,
    @SerializedName("EMPTY")
    EMPTY,
    @SerializedName("MODERATED")
    MODERATED,
    @SerializedName("DELETED")
    DELETED,
    @SerializedName("PENDING")
    PENDING,
    @SerializedName("VIOLATION")
    VIOLATION
}