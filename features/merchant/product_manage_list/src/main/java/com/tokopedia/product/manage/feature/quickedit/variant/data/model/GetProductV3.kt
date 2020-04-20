package com.tokopedia.product.manage.feature.quickedit.variant.data.model

import com.google.gson.annotations.SerializedName

data class GetProductV3 (
    @SerializedName("productName")
    val productName: String,
    @SerializedName("variant")
    val variant: Variant
)