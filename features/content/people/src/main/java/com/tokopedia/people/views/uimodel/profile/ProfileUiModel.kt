package com.tokopedia.people.views.uimodel.profile

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
    val stats: ProfileStatsUiModel,
    val shareLink: LinkUiModel,
    val liveInfo: LivePlayChannelUiModel,
    val isBlocking: Boolean,
    val isBlockedBy: Boolean,
    val badge: Badge = Badge.Empty
) {
    sealed class Badge {

        object Empty: Badge()

        data class Verified(
            val url: String,
            val detail: Detail?
        ): Badge() {

            data class Detail(
                val title: String,
                val desc: String
            )
        }
    }

    companion object {
        val Empty: ProfileUiModel
            get() = ProfileUiModel(
                userID = "",
                encryptedUserID = "",
                imageCover = "",
                name = "",
                username = "",
                biography = "",
                stats = ProfileStatsUiModel.Empty,
                shareLink = LinkUiModel.Empty,
                liveInfo = LivePlayChannelUiModel.Empty,
                isBlocking = false,
                isBlockedBy = false
            )
    }
}
