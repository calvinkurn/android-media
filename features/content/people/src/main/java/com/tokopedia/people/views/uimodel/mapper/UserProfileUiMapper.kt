package com.tokopedia.people.views.uimodel.mapper

import com.tokopedia.content.common.model.Creation
import com.tokopedia.feedcomponent.domain.model.UserFeedPostsModel
import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.people.model.ProfileHeaderBase
import com.tokopedia.people.model.UserPostModel
import com.tokopedia.people.model.UserProfileIsFollow
import com.tokopedia.people.model.UserProfileTabModel
import com.tokopedia.people.model.VideoPostReimderModel
import com.tokopedia.people.model.GetProfileSettingsResponse
import com.tokopedia.people.model.GetUserReviewListResponse
import com.tokopedia.people.model.SetLikeStatusResponse
import com.tokopedia.people.views.uimodel.ProfileSettingsUiModel
import com.tokopedia.people.views.uimodel.UserReviewUiModel
import com.tokopedia.people.views.uimodel.content.UserFeedPostsUiModel
import com.tokopedia.people.views.uimodel.content.UserPlayVideoUiModel
import com.tokopedia.people.views.uimodel.profile.FollowInfoUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileCreationInfoUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileTabUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
interface UserProfileUiMapper {
    fun mapUserProfile(response: ProfileHeaderBase): ProfileUiModel

    fun mapFollowInfo(response: UserProfileIsFollow): FollowInfoUiModel

    fun mapCreationInfo(response: Creation): ProfileCreationInfoUiModel

    fun mapUpdateReminder(response: VideoPostReimderModel): MutationUiModel

    fun mapProfileTab(response: UserProfileTabModel): ProfileTabUiModel

    fun mapFeedPosts(response: UserFeedPostsModel): UserFeedPostsUiModel

    fun mapPlayVideo(response: UserPostModel): UserPlayVideoUiModel

    fun mapProfileSettings(response: GetProfileSettingsResponse): List<ProfileSettingsUiModel>

    fun mapUserReviewList(
        response: GetUserReviewListResponse,
        currentPage: Int,
    ): UserReviewUiModel

    fun mapSetLikeStatus(response: SetLikeStatusResponse): UserReviewUiModel.LikeDislike
}
