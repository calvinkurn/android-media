package com.tokopedia.content.common.onboarding.view.uimodel.action

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
sealed interface UGCOnboardingAction {

    data class InputUsername(
        val username: String
    ) : UGCOnboardingAction

    object CheckUsername : UGCOnboardingAction

    object CheckTnc : UGCOnboardingAction

    object ClickNext : UGCOnboardingAction
}