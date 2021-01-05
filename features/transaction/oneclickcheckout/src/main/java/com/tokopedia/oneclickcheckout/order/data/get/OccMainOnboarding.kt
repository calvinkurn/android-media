package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName

data class OccMainOnboarding(
        @SerializedName("force_show_coachmark")
        val isForceShowCoachMark: Boolean = false,
        @SerializedName("show_onboarding_ticker")
        val isShowOnboardingTicker: Boolean = false,
        @SerializedName("coachmark_type")
        val coachmarkType: Int = 0,
        @SerializedName("onboarding_ticker")
        val onboardingTicker: OccOnboardingTicker = OccOnboardingTicker(),
        @SerializedName("onboarding_coachmark")
        val onboardingCoachMark: OccOnboardingCoachMark = OccOnboardingCoachMark()
) {
        companion object {
                internal const val COACHMARK_TYPE_NEW_BUYER_BEFORE_CREATE_PROFILE = 1
                internal const val COACHMARK_TYPE_NEW_BUYER_AFTER_CREATE_PROFILE = 2
                internal const val COACHMARK_TYPE_EXISTING_USER_ONE_PROFILE = 3
                internal const val COACHMARK_TYPE_EXISTING_USER_MULTI_PROFILE = 4
        }
}

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