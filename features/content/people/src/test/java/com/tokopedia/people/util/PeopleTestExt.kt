package com.tokopedia.people.util

import com.tokopedia.people.views.uimodel.ProfileSettingsUiModel
import com.tokopedia.people.views.uimodel.event.UserProfileSettingsEvent
import com.tokopedia.people.views.uimodel.event.UserProfileUiEvent
import com.tokopedia.people.views.uimodel.state.UserProfileUiState

/**
 * Created By : Jonathan Darwin on July 05, 2022
 */
infix fun UserProfileUiState.andThen(fn: UserProfileUiState.() -> Unit) {
    fn()
}

infix fun List<UserProfileUiEvent>.andThen(fn: List<UserProfileUiEvent>.() -> Unit) {
    fn()
}

infix fun Pair<UserProfileUiState, List<UserProfileUiEvent>>.andThen(
    fn: Pair<UserProfileUiState, List<UserProfileUiEvent>>
    .(UserProfileUiState, List<UserProfileUiEvent>) -> Unit,
) {
    fn(first, second)
}


infix fun ProfileSettingsUiModel.andThen(fn: ProfileSettingsUiModel.() -> Unit) {
    fn()
}
