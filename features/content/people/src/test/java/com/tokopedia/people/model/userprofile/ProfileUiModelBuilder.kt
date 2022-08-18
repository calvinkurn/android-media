package com.tokopedia.people.model.userprofile

import com.tokopedia.people.views.uimodel.profile.LinkUiModel
import com.tokopedia.people.views.uimodel.profile.LivePlayChannelUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileStatsUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel

/**
 * Created By : Jonathan Darwin on July 05, 2022
 */
class ProfileUiModelBuilder {

    fun buildProfile(
        userID: String = "1",
        encryptedUserID: String = "1",
        imageCover: String = "",
        name: String = "Jonathan Darwin",
        username: String = "jonathandarwin",
        biography: String = "testing",
        badges: List<Any?> = emptyList(),
        stats: ProfileStatsUiModel = ProfileStatsUiModel.Empty,
        shareLink: LinkUiModel = LinkUiModel.Empty,
        liveInfo: LivePlayChannelUiModel = LivePlayChannelUiModel.Empty,
    ) = ProfileUiModel(
        userID = userID,
        encryptedUserID = encryptedUserID,
        imageCover = imageCover,
        name = name,
        username = username,
        biography = biography,
        badges = badges,
        stats = stats,
        shareLink = shareLink,
        liveInfo = liveInfo,
    )
}