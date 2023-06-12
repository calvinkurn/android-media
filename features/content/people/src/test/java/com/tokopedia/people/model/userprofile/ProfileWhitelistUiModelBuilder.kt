package com.tokopedia.people.model.userprofile

import com.tokopedia.people.views.uimodel.profile.ProfileCreationInfoUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileWhitelistUiModel

/**
 * Created By : Jonathan Darwin on July 05, 2022
 */
class ProfileWhitelistUiModelBuilder {

    fun buildNoWhitelist() = buildWhitelist(
        isWhitelist = false,
        hasUsername = false,
        hasAcceptTnc = false,
    )

    fun buildOnlyWhitelist() = buildWhitelist(
        isWhitelist = true,
        hasUsername = false,
        hasAcceptTnc = false,
    )

    fun buildHasUsername() = buildWhitelist(
        isWhitelist = true,
        hasUsername = true,
        hasAcceptTnc = false,
    )

    fun buildHasAcceptTnc() = buildWhitelist(
        isWhitelist = true,
        hasUsername = true,
        hasAcceptTnc = true,
    )

    fun buildHasNotAcceptTnc() = buildWhitelist(
        isWhitelist = false,
        hasUsername = false,
        hasAcceptTnc = false,
    )

    private fun buildWhitelist(
        isWhitelist: Boolean = false,
        hasUsername: Boolean = false,
        hasAcceptTnc: Boolean = false,
    ) = ProfileWhitelistUiModel(
        isWhitelist = isWhitelist,
        hasUsername = hasUsername,
        hasAcceptTnc = hasAcceptTnc,
    )

    fun buildCreationInfoModel(): ProfileCreationInfoUiModel {
        return ProfileCreationInfoUiModel(
            isActive = true,
            showPost = true,
            showLiveStream = true,
            showShortVideo = true,
        )
    }
}
