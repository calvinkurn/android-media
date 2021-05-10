package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

class BoMetadata(
        @SerializedName("bo_type")
        val boType: Int = 0,
        @SerializedName("bo_eligibilities")
        val boEligibilities: List<BoEligibility> = emptyList(),
        @SerializedName("additional_attributes")
        val additionalAttributes: List<BoAdditionalAttribute> = emptyList()
)

class BoEligibility(
        @SerializedName("key")
        val key: String = "",
        @SerializedName("value")
        val value: String = ""
)

class BoAdditionalAttribute(
        @SerializedName("key")
        val key: String = "",
        @SerializedName("value")
        val value: String = ""
)