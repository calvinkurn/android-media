package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName

class OccMainOnboardingResponse(
        @SerializedName("force_show_coachmark")
        val isForceShowCoachMark: Boolean = false,
        @SerializedName("show_onboarding_ticker")
        val isShowOnboardingTicker: Boolean = false,
        @SerializedName("coachmark_type")
        val coachmarkType: Int = 0,
        @SerializedName("onboarding_ticker")
        val onboardingTicker: OccOnboardingTickerResponse = OccOnboardingTickerResponse(),
        @SerializedName("onboarding_coachmark")
        val onboardingCoachMark: OccOnboardingCoachMarkResponse = OccOnboardingCoachMarkResponse()
)

class OccOnboardingTickerResponse(
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

class OccOnboardingCoachMarkResponse(
        @SerializedName("skip_button_text")
        val skipButtonText: String = "",
        @SerializedName("detail")
        val details: List<OccOnboardingCoachMarkDetailResponse> = emptyList()
)

class OccOnboardingCoachMarkDetailResponse(
        @SerializedName("step")
        val step: Int = 0,
        @SerializedName("title")
        val title: String = "",
        @SerializedName("message")
        val message: String = ""
)