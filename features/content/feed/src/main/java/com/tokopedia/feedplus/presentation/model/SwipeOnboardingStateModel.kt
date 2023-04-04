package com.tokopedia.feedplus.presentation.model

/**
 * Created by kenny.hadisaputra on 21/03/23
 */
data class SwipeOnboardingStateModel(
    val onDataLoaded: Boolean,
    val isReadyToShow: Boolean,
    val hasShown: Boolean
) {
    val isEligibleToShow: Boolean
        get() = onDataLoaded && isReadyToShow && !hasShown

    companion object {
        val Empty: SwipeOnboardingStateModel
            get() = SwipeOnboardingStateModel(
                onDataLoaded = false,
                isReadyToShow = false,
                hasShown = false,
            )
    }
}
