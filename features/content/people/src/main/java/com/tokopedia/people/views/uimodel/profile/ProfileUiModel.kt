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
    val badges: List<String>,
    val stats: ProfileStatsUiModel,
    val shareLink: LinkUiModel,
    val liveInfo: LivePlayChannelUiModel,
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
            )
    }
}
