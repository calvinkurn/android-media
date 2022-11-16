package com.tokopedia.people.views.uimodel.action

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
sealed interface UserProfileAction {

    data class LoadProfile(val isRefresh: Boolean = false) : UserProfileAction

    data class LoadFeedPosts(val cursor: String) : UserProfileAction

    data class LoadPlayVideo(val cursor: String) : UserProfileAction

    data class ClickFollowButton(val isFromLogin: Boolean) : UserProfileAction

    data class ClickUpdateReminder(val isFromLogin: Boolean) : UserProfileAction

    data class ClickFollowButtonShopRecom(val itemID: Long) : UserProfileAction

    data class RemoveShopRecomItem(val itemID: Long) : UserProfileAction

    data class SaveReminderActivityResult(
        val channelId: String,
        val position: Int,
        val isActive: Boolean,
    ) : UserProfileAction

    data class LoadNextPageShopRecom(val nextCurSor: String) : UserProfileAction

    object RemoveReminderActivityResult : UserProfileAction

    object LoadProfileTab: UserProfileAction
}
