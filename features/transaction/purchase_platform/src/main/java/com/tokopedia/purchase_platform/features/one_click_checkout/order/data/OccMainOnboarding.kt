package com.tokopedia.purchase_platform.features.one_click_checkout.order.data

import com.google.gson.annotations.SerializedName

data class OccMainOnboarding(
        @SerializedName("force_show_coachmark")
        val isForceShowCoachMark: Boolean = false,
        @SerializedName("show_onboarding_ticker")
        val isShowOnboardingTicker: Boolean = false,
        @SerializedName("onboarding_ticker")
        val onboardingTicker: OccOnboardingTicker = OccOnboardingTicker(),
        @SerializedName("onboarding_coachmark")
        val onboardingCoachMark: OccOnboardingCoachMark = OccOnboardingCoachMark()
)