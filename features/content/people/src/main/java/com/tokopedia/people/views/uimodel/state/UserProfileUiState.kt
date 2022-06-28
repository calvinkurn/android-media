package com.tokopedia.people.views.uimodel.state

import com.tokopedia.people.views.uimodel.profile.ProfileStatsUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileWhitelistUiModel

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
data class UserProfileUiState(
    val profileInfo: ProfileUiModel,
    val profileStats: ProfileStatsUiModel,
    val profileWhitelist: ProfileWhitelistUiModel,
)