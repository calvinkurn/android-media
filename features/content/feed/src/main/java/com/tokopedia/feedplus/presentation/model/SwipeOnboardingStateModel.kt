package com.tokopedia.feedplus.presentation.model

/**
 * Created by kenny.hadisaputra on 21/03/23
 */
data class SwipeOnboardingStateModel(
    val isReadyToShow: Boolean,
    val isDataEligibleForOnboarding: Boolean,
    val hasShown: Boolean
) {
    val isEligibleToShow: Boolean
        get() = isReadyToShow && isDataEligibleForOnboarding && !hasShown

    companion object {
        val Empty: SwipeOnboardingStateModel
            get() = SwipeOnboardingStateModel(
                isReadyToShow = false,
                isDataEligibleForOnboarding = false,
                hasShown = false,
            )
    }
}
