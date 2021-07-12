package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName

class ProfileResponse(
        @SerializedName("is_changed_profile")
        val isChangedProfile: Boolean = false,
        @SerializedName("profile_revamp_wording")
        val profileRevampWording: String = "",
        @SerializedName("profile_id")
        val profileId: Int = 0,
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("enable")
        val enable: Boolean = true,
        @SerializedName("message")
        val message: String = "",
        @SerializedName("address")
        val address: Address = Address(),
        @SerializedName("payment")
        val payment: Payment = Payment(),
        @SerializedName("shipment")
        val shipment: Shipment = Shipment()
)