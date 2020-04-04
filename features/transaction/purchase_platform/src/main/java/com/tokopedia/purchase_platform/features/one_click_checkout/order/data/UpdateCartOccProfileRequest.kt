package com.tokopedia.purchase_platform.features.one_click_checkout.order.data

import com.google.gson.annotations.SerializedName

data class UpdateCartOccProfileRequest(
        @SerializedName("profile_id")
        val profileId: String = "",
        @SerializedName("gateway_code")
        val gatewayCode: String = "",
        @SerializedName("metadata")
        val metadata: String = "",
        @SerializedName("service_id")
        val serviceId: Int = 0,
        @SerializedName("address_id")
        val addressId: String = ""
)