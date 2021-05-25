package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class VariantDescriptionDetail(
        @SerializedName("variant_description")
        val variantDescription: String = "",
        @SerializedName("variant_name")
        val variantName: List<String> = emptyList()
)