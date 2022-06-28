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
    var biography: String,
    val badges: List<Any?>,
    val isSelfProfile: Boolean,
    val shareLink: LinkUiModel,
    val followInfo: FollowInfoUiModel,
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
                isSelfProfile = false,
                shareLink = LinkUiModel.Empty,
                followInfo = FollowInfoUiModel.Empty,
                liveInfo = LivePlayChannelUiModel.Empty,
            )
    }
}