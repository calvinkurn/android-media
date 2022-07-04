package com.tokopedia.feedcomponent.onboarding.view.uimodel.event

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
sealed interface FeedUGCOnboardingUiEvent {

    object ErrorAcceptTnc : FeedUGCOnboardingUiEvent

    object ErrorCheckUsername : FeedUGCOnboardingUiEvent
}