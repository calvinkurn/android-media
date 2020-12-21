package com.tokopedia.product.manage.common.feature.variant.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetProductV3 (
    @Expose
    @SerializedName("productName")
    val productName: String,
    @Expose
    @SerializedName("variant")
    val variant: Variant
)