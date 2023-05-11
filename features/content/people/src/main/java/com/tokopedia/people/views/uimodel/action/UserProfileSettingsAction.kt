package com.tokopedia.people.views.uimodel.action

/**
 * Created By : Jonathan Darwin on May 11, 2023
 */
sealed interface UserProfileSettingsAction {

    object GetProfileSettings : UserProfileSettingsAction

    data class SetShowReview(val isShow: Boolean) : UserProfileSettingsAction

}
