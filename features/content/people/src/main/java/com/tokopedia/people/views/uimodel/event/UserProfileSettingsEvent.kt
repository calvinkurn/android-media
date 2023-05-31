package com.tokopedia.people.views.uimodel.event

/**
 * Created By : Jonathan Darwin on May 11, 2023
 */
sealed interface UserProfileSettingsEvent {

    data class ErrorSetShowReview(val throwable: Throwable) : UserProfileSettingsEvent
}
