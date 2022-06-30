package com.tokopedia.people.views.uimodel.action

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
sealed interface UserProfileAction {

    data class LoadProfile(val isRefresh: Boolean = false) : UserProfileAction

    data class LoadPlayVideo(val cursor: String) : UserProfileAction

    data class ClickFollowButton(val isFromLogin: Boolean) : UserProfileAction

    data class ClickUpdateReminder(
        val channelId: String,
        val isActive: Boolean,
    ) : UserProfileAction
}