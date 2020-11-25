package com.tokopedia.checkout.data.model.response.shipment_address_form

import com.google.gson.annotations.SerializedName

data class VariantDescriptionDetail(
        @SerializedName("variant_name")
        val variantName: List<String> = emptyList(),
        @SerializedName("variant_description")
        val variantDescription: String = ""
)