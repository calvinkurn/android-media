package com.tokopedia.people.data

import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModel
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
interface UserProfileRepository {

    suspend fun getProfile(username: String): ProfileUiModel

    suspend fun getFollowInfo(profileIdList: List<String>): FollowInfoUiModel

    suspend fun getCreationInfo(): ProfileCreationInfoUiModel

    suspend fun updateReminder(
        channelId: String,
        isActive: Boolean,
    ): MutationUiModel

    suspend fun getFeedPosts(
        userID: String,
        cursor: String,
        limit: Int,
    ): UserFeedPostsUiModel

    suspend fun getPlayVideo(
        username: String,
        cursor: String,
        isSelfProfile: Boolean,
    ): UserPlayVideoUiModel

    suspend fun getShopRecom(cursor: String): ShopRecomUiModel

    suspend fun getUserProfileTab(userID: String): ProfileTabUiModel

    suspend fun blockUser(userId: String)

    suspend fun unblockUser(userId: String)

    suspend fun deletePlayChannel(
        channelId: String,
        userId: String,
    ): String

    suspend fun getProfileSettings(
        userID: String,
    ): List<ProfileSettingsUiModel>

    suspend fun setShowReview(
        userID: String,
        settingID: String,
        isShow: Boolean,
    ): Boolean

    suspend fun getUserReviewList(
        userID: String,
        limit: Int,
        page: Int
    ): UserReviewUiModel

    suspend fun setLikeStatus(
        feedbackID: String,
        isLike: Boolean,
    ): UserReviewUiModel.LikeDislike

}
