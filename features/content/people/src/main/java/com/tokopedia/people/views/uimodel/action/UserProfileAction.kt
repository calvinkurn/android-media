package com.tokopedia.people.views.uimodel.action

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
sealed interface UserProfileAction {

    data class LoadProfile(
        val username: String,
        val isRefresh: Boolean = false,
    ) : UserProfileAction

    object ClickFollowButton: UserProfileAction

    data class ClickUpdateReminder(
        val channelId: String,
        val isActive: Boolean,
    ) : UserProfileAction
}