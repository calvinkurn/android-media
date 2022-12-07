package com.tokopedia.people.views.uimodel.profile

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
data class ProfileWhitelistUiModel(
    val isWhitelist: Boolean,
    val hasUsername: Boolean,
    val hasAcceptTnc: Boolean,
) {
    companion object {
        val Empty: ProfileWhitelistUiModel
            get() = ProfileWhitelistUiModel(
                isWhitelist = false,
                hasUsername = false,
                hasAcceptTnc = false,
            )
    }
}
