package com.tokopedia.feedcomponent.util

import com.tokopedia.feedcomponent.onboarding.view.uimodel.event.FeedUGCOnboardingUiEvent
import com.tokopedia.feedcomponent.onboarding.view.uimodel.state.FeedUGCOnboardingUiState

/**
 * Created By : Jonathan Darwin on July 06, 2022
 */
infix fun FeedUGCOnboardingUiState.andThen(fn: FeedUGCOnboardingUiState.() -> Unit) {
    fn()
}

infix fun List<FeedUGCOnboardingUiEvent>.andThen(fn: List<FeedUGCOnboardingUiEvent>.() -> Unit) {
    fn()
}

infix fun Pair<FeedUGCOnboardingUiState, List<FeedUGCOnboardingUiEvent>>.andThen(
    fn: Pair<FeedUGCOnboardingUiState, List<FeedUGCOnboardingUiEvent>>.
        (FeedUGCOnboardingUiState,  List<FeedUGCOnboardingUiEvent>) -> Unit
) {
    fn(first, second)
}