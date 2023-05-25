package com.tokopedia.people.views.uimodel.state

import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModel
import com.tokopedia.people.views.uimodel.content.UserFeedPostsUiModel
import com.tokopedia.people.views.uimodel.content.UserPlayVideoUiModel
import com.tokopedia.people.views.uimodel.profile.FollowInfoUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileCreationButtonUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileTabUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileType
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
data class UserProfileUiState(
    val profileInfo: ProfileUiModel,
    val followInfo: FollowInfoUiModel,
    val profileType: ProfileType,
    val showCreationButton: ProfileCreationButtonUiModel,
    val shopRecom: ShopRecomUiModel,
    val profileTab: ProfileTabUiModel,
    val feedPostsContent: UserFeedPostsUiModel,
    val videoPostsContent: UserPlayVideoUiModel,
    val isLoading: Boolean,
    val error: Throwable?,
)
