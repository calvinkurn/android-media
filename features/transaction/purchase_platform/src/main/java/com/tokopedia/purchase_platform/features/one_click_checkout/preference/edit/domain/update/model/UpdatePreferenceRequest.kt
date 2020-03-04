package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.update.model

import com.google.gson.annotations.SerializedName

data class UpdatePreferenceRequest (
        @SerializedName("profile_id")
        val profileId: Int,
        @SerializedName("address_id")
        val addressId: Int,
        @SerializedName("service_id")
        val serviceId: Int,
        @SerializedName("gatewayCode")
        val gatewayCode: String,
        @SerializedName("metadata")
        val metadata: String? = null
)