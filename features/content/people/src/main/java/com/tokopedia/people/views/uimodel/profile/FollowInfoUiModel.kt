package com.tokopedia.people.views.uimodel.profile

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
data class FollowInfoUiModel(
    val userID: String,
    val encryptedUserID: String,
    val status: Boolean,
) {
    companion object {
        val Empty: FollowInfoUiModel
            get() = FollowInfoUiModel(
                userID = "",
                encryptedUserID = "",
                status = false,
            )
    }
}
