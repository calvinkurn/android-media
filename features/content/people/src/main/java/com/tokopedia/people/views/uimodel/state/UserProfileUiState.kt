package com.tokopedia.people.views.uimodel.state

import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModel
import com.tokopedia.people.views.uimodel.ProfileSettingsUiModel
import com.tokopedia.people.views.uimodel.UserReviewUiModel
import com.tokopedia.people.views.uimodel.content.UserFeedPostsUiModel
import com.tokopedia.people.views.uimodel.content.UserPlayVideoUiModel
import com.tokopedia.people.views.uimodel.profile.FollowInfoUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileCreationInfoUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileTabState
import com.tokopedia.people.views.uimodel.profile.ProfileType
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
data class UserProfileUiState(
    val profileInfo: ProfileUiModel,
    val followInfo: FollowInfoUiModel,
    val profileType: ProfileType,
    val creationInfo: ProfileCreationInfoUiModel,
    val shopRecom: ShopRecomUiModel,
    val profileTab: ProfileTabState,
    val feedPostsContent: UserFeedPostsUiModel,
    val videoPostsContent: UserPlayVideoUiModel,
    val reviewContent: UserReviewUiModel,
    val isLoading: Boolean,
    val error: Throwable?,
    val reviewSettings: ProfileSettingsUiModel,
)
