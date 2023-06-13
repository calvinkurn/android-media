package com.tokopedia.people.model.userprofile

import com.tokopedia.people.views.uimodel.profile.FollowInfoUiModel

/**
 * Created By : Jonathan Darwin on July 05, 2022
 */
class FollowInfoUiModelBuilder {

    fun buildFollowInfo(
        userID: String = "",
        encryptedUserID: String = "",
        status: Boolean = false,
    ) = FollowInfoUiModel(
        userID = userID,
        encryptedUserID = encryptedUserID,
        status = status,
    )

    fun buildEmptyFollowInfo() = FollowInfoUiModel.Empty
}
