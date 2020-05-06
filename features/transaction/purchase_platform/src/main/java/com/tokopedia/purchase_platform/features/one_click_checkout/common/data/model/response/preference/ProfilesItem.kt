package com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.preference

import com.google.gson.annotations.SerializedName

data class ProfilesItem(
        @SerializedName("profile_id")
        val profileId: Int = 0,
        @SerializedName("status")
        val status: Int = 1,
        @SerializedName("address")
        val address: Address? = null,
        @SerializedName("payment")
        val payment: Payment? = null,
        @SerializedName("shipment")
        val shipment: Shipment? = null
)
