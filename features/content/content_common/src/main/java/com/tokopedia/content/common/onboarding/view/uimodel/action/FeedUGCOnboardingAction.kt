package com.tokopedia.content.common.onboarding.view.uimodel.action

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
sealed interface FeedUGCOnboardingAction {

    data class InputUsername(
        val username: String
    ) : FeedUGCOnboardingAction

    object CheckUsername : FeedUGCOnboardingAction

    object CheckTnc : FeedUGCOnboardingAction

    object ClickNext : FeedUGCOnboardingAction
}