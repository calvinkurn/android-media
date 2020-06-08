package com.tokopedia.purchase_platform.features.one_click_checkout.order.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UpdateCartOccProfileRequest(
        @SerializedName("profile_id")
        @Expose
        val profileId: String = "",
        @SerializedName("gateway_code")
        @Expose
        val gatewayCode: String = "",
        @SerializedName("metadata")
        @Expose
        val metadata: String = "",
        @SerializedName("service_id")
        @Expose
        val serviceId: Int = 0,
        @SerializedName("address_id")
        @Expose
        val addressId: String = ""
)