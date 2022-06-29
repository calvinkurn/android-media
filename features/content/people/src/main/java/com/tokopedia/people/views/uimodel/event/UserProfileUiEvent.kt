package com.tokopedia.people.views.uimodel.event

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
sealed interface UserProfileUiEvent {

    data class SuccessUpdateReminder(val message: String): UserProfileUiEvent
    data class ErrorUpdateReminder(val throwable: Throwable): UserProfileUiEvent
}