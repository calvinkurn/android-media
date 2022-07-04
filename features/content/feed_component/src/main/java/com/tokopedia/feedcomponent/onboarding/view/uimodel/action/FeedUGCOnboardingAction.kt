package com.tokopedia.feedcomponent.onboarding.view.uimodel.action

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
sealed interface FeedUGCOnboardingAction {

    data class InputUsername(
        val username: String
    ) : FeedUGCOnboardingAction

    data class CheckTnc(val isChecked: Boolean) : FeedUGCOnboardingAction

    object ClickNext : FeedUGCOnboardingAction
}