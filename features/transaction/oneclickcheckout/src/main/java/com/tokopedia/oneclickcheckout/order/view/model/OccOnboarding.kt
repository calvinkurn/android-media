package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.oneclickcheckout.common.data.model.OrderItem

data class OccOnboarding(
        val isForceShowCoachMark: Boolean = false,
        val isShowOnboardingTicker: Boolean = false,
        val coachmarkType: Int = 0,
        val onboardingTicker: OccOnboardingTicker = OccOnboardingTicker(),
        val onboardingCoachMark: OccOnboardingCoachMark = OccOnboardingCoachMark()
) : OrderItem {
    companion object {
        internal const val COACHMARK_TYPE_NEW_BUYER_REMOVE_PROFILE = 5
    }
}

data class OccOnboardingTicker(
        val title: String = "",
        val message: String = "",
        val image: String = "",
        val showActionButton: Boolean = false,
        val actionText: String = ""
)

data class OccOnboardingCoachMark(
        val skipButtonText: String = "",
        val details: List<OccOnboardingCoachMarkDetail> = emptyList()
)

data class OccOnboardingCoachMarkDetail(
        val step: Int = 0,
        val title: String = "",
        val message: String = ""
)