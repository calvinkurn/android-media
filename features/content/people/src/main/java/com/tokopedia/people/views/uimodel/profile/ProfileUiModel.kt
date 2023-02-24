package com.tokopedia.people.views.uimodel.profile

import com.tokopedia.library.baseadapter.BaseItem

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
data class ProfileUiModel(
    val userID: String,
    val encryptedUserID: String,
    val imageCover: String,
    val name: String,
    val username: String,
    val biography: String,
    val badges: List<String>,
    val stats: ProfileStatsUiModel,
    val shareLink: LinkUiModel,
    val liveInfo: LivePlayChannelUiModel,
    val isBlocking: Boolean,
    val isBlockedBy: Boolean,
) {

    companion object {
        val Empty: ProfileUiModel
            get() = ProfileUiModel(
                userID = "",
                encryptedUserID = "",
                imageCover = "",
                name = "",
                username = "",
                biography = "",
                badges = emptyList(),
                stats = ProfileStatsUiModel.Empty,
                shareLink = LinkUiModel.Empty,
                liveInfo = LivePlayChannelUiModel.Empty,
                isBlocking = false,
                isBlockedBy = false,
            )
    }

    sealed class PeopleUiModel : BaseItem()

    data class ShopUiModel(
        val id: String,
        val logoUrl: String,
        val badgeUrl: String,
        val name: String,
        val isFollowed: Boolean,
        val appLink: String,
    ): PeopleUiModel()

    data class UserUiModel(
        val id: String,
        val encryptedId: String,
        val photoUrl: String,
        val name: String,
        val username: String,
        val isFollowed: Boolean,
        val isMySelf: Boolean,
        val appLink: String,
    ): PeopleUiModel()
}
