package com.tokopedia.oneclickcheckout.order.data.get

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

data class OccOnboardingTicker(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("message")
        val message: String = "",
        @SerializedName("image")
        val image: String = "",
        @SerializedName("show_coachmark_link_text")
        val showActionButton: Boolean = false,
        @SerializedName("coachmark_link_text")
        val actionText: String = ""
)

data class OccOnboardingCoachMark(
        @SerializedName("skip_button_text")
        val skipButtonText: String = "",
        @SerializedName("detail")
        val details: List<OccOnboardingCoachMarkDetail> = emptyList()
)

data class OccOnboardingCoachMarkDetail(
        @SerializedName("step")
        val step: Int = 0,
        @SerializedName("title")
        val title: String = "",
        @SerializedName("message")
        val message: String = ""
)