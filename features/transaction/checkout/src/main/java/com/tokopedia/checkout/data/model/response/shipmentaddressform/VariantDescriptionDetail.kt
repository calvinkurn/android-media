package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class VariantDescriptionDetail(
    @SerializedName("variant_description")
    val variantDescription: String = ""
)
