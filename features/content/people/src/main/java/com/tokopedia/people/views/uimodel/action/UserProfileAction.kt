package com.tokopedia.people.views.uimodel.action

import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
sealed interface UserProfileAction {

    data class ClickFollowButton(val isFromLogin: Boolean) : UserProfileAction

    data class ClickFollowButtonShopRecom(val itemID: Long) : UserProfileAction

    data class ClickUpdateReminder(val isFromLogin: Boolean) : UserProfileAction

    data class LoadFeedPosts(val cursor: String = "", val isRefresh: Boolean = false) : UserProfileAction

    data class LoadPlayVideo(val isRefresh: Boolean = false) : UserProfileAction

    data class LoadProfile(val isRefresh: Boolean = false) : UserProfileAction

    data class LoadNextPageShopRecom(val nextCurSor: String) : UserProfileAction

    data class RemoveShopRecomItem(val itemID: Long) : UserProfileAction

    data class SaveReminderActivityResult(val channel: PlayWidgetChannelUiModel) : UserProfileAction

    object BlockUser : UserProfileAction
    object UnblockUser : UserProfileAction

    data class DeletePlayChannel(val channel: PlayWidgetChannelUiModel) : UserProfileAction

    data class UpdatePlayChannelInfo(
        val channelId: String,
        val totalView: String,
        val isReminderSet: Boolean,
    ) : UserProfileAction

    data class ClickPlayVideoMenuAction(val channel: PlayWidgetChannelUiModel) : UserProfileAction
}
