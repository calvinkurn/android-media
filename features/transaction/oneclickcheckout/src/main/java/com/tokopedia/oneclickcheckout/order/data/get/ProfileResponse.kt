package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName
import com.tokopedia.oneclickcheckout.common.data.model.Address
import com.tokopedia.oneclickcheckout.common.data.model.Payment
import com.tokopedia.oneclickcheckout.common.data.model.Shipment

data class ProfileResponse(
        @SerializedName("onboarding_header_message")
        val onboardingHeaderMessage: String = "",
        @SerializedName("onboarding_component")
        val onboardingComponent: OnboardingComponentResponse = OnboardingComponentResponse(),
        @SerializedName("has_preference")
        val hasPreference: Boolean = false,
        @SerializedName("is_changed_profile")
        val isChangedProfile: Boolean = false,
        @SerializedName("profile_revamp_wording")
        val profileRevampWording: String = "",
        @SerializedName("is_recom")
        val isRecom: Boolean = false,
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