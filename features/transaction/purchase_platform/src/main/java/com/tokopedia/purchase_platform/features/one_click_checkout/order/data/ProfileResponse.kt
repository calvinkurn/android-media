package com.tokopedia.purchase_platform.features.one_click_checkout.order.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.preference.Address
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.preference.Payment
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.preference.Shipment

data class ProfileResponse(
        @SerializedName("onboarding_header_message")
        val onboardingHeaderMessage: String = "",
        @SerializedName("onboarding_component")
        val onboardingComponent: OnboardingComponentResponse = OnboardingComponentResponse(),
        @SerializedName("has_preference")
        val hasPreference: Boolean = false,
        @SerializedName("is_changed_profile")
        val isChangedProfile: Boolean = false,
        @SerializedName("profile_id")
        val profileId: Int = 0,
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("address")
        val address: Address = Address(),
        @SerializedName("payment")
        val payment: Payment = Payment(),
        @SerializedName("shipment")
        val shipment: Shipment = Shipment()
)