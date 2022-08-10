package com.tokopedia.people.views.uimodel.state

import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomUiModel
import com.tokopedia.people.views.uimodel.profile.FollowInfoUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileType
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileWhitelistUiModel

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
data class UserProfileUiState(
    val profileInfo: ProfileUiModel,
    val followInfo: FollowInfoUiModel,
    val profileType: ProfileType,
    val profileWhitelist: ProfileWhitelistUiModel,
    val shopRecom: ShopRecomUiModel,
)