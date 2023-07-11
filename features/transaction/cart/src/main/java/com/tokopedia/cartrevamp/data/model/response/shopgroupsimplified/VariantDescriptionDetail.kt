package com.tokopedia.cartrevamp.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class VariantDescriptionDetail(
    @SerializedName("variant_name")
    val variantNames: List<String> = emptyList()
)
