package com.tokopedia.content.common.onboarding.view.uimodel.state

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
data class UGCOnboardingUiState(
    val username: String,
    val usernameState: UsernameState,
    val isCheckTnc: Boolean,
    val isSubmit: Boolean,
    val hasAcceptTnc: Boolean,
    // .....
)

sealed interface UsernameState {

    object Unknown : UsernameState
    object Loading : UsernameState
    data class Invalid(val message: String) : UsernameState
    object Valid : UsernameState
}
