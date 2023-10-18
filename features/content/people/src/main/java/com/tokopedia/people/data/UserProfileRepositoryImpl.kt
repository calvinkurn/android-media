package com.tokopedia.people.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.model.FeedXHeaderRequestFields.CREATION
import com.tokopedia.content.common.usecase.FeedXHeaderUseCase
import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.feedcomponent.domain.usecase.GetUserProfileFeedPostsUseCase
import com.tokopedia.feedcomponent.domain.usecase.shoprecom.ShopRecomUseCase
import com.tokopedia.feedcomponent.domain.usecase.shoprecom.ShopRecomUseCase.Companion.VAL_LIMIT
import com.tokopedia.feedcomponent.domain.usecase.shoprecom.ShopRecomUseCase.Companion.VAL_SCREEN_NAME_USER_PROFILE
import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.feedcomponent.shoprecom.mapper.ShopRecomUiMapper
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModel
import com.tokopedia.people.model.GetProfileSettingsRequest
import com.tokopedia.people.model.GetUserReviewListRequest
import com.tokopedia.people.model.SetLikeStatusRequest
import com.tokopedia.people.model.SetProfileSettingsRequest
import com.tokopedia.people.views.uimodel.ProfileSettingsUiModel
import com.tokopedia.people.views.uimodel.UserReviewUiModel
import com.tokopedia.people.domains.GetUserProfileTabUseCase
import com.tokopedia.people.domains.PlayPostContentUseCase
import com.tokopedia.people.domains.PostBlockUserUseCase
import com.tokopedia.people.domains.ProfileTheyFollowedUseCase
import com.tokopedia.people.domains.UserDetailsUseCase
import com.tokopedia.people.domains.VideoPostReminderUseCase
import com.tokopedia.people.domains.GetProfileSettingsUseCase
import com.tokopedia.people.domains.SetProfileSettingsUseCase
import com.tokopedia.people.domains.GetUserReviewListUseCase
import com.tokopedia.people.domains.SetLikeStatusUseCase
import com.tokopedia.people.views.uimodel.content.UserFeedPostsUiModel
import com.tokopedia.people.views.uimodel.content.UserPlayVideoUiModel
import com.tokopedia.people.views.uimodel.mapper.UserProfileUiMapper
import com.tokopedia.people.views.uimodel.profile.FollowInfoUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileCreationInfoUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileTabUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.types.PlayChannelStatusType
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
class UserProfileRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val mapper: UserProfileUiMapper,
    private val shopRecomMapper: ShopRecomUiMapper,
    private val userDetailsUseCase: UserDetailsUseCase,
    private val playVodUseCase: PlayPostContentUseCase,
    private val profileIsFollowing: ProfileTheyFollowedUseCase,
    private val videoPostReminderUseCase: VideoPostReminderUseCase,
    private val shopRecomUseCase: ShopRecomUseCase,
    private val getUserProfileTabUseCase: GetUserProfileTabUseCase,
    private val getUserProfileFeedPostsUseCase: GetUserProfileFeedPostsUseCase,
    private val postBlockUserUseCase: PostBlockUserUseCase,
    private val updateChannelUseCase: UpdateChannelUseCase,
    private val feedXHeaderUseCase: FeedXHeaderUseCase,
    private val getProfileSettingsUseCase: GetProfileSettingsUseCase,
    private val setProfileSettingsUseCase: SetProfileSettingsUseCase,
    private val getUserReviewListUseCase: GetUserReviewListUseCase,
    private val setLikeStatusUseCase: SetLikeStatusUseCase,
) : UserProfileRepository {

    override suspend fun getProfile(username: String): ProfileUiModel {
        return withContext(dispatcher.io) {
            val result = userDetailsUseCase.executeOnBackground(username)

            mapper.mapUserProfile(result)
        }
    }

    override suspend fun getFollowInfo(profileIdList: List<String>): FollowInfoUiModel {
        return withContext(dispatcher.io) {
            val result = profileIsFollowing.executeOnBackground(profileIdList)

            mapper.mapFollowInfo(result)
        }
    }

    override suspend fun getCreationInfo(): ProfileCreationInfoUiModel {
        return withContext(dispatcher.io) {
            feedXHeaderUseCase.setRequestParams(
                FeedXHeaderUseCase.createParam(listOf(CREATION.value))
            )
            val result = feedXHeaderUseCase.executeOnBackground()
            mapper.mapCreationInfo(result.feedXHeaderData.data.creation)
        }
    }

    override suspend fun updateReminder(channelId: String, isActive: Boolean): MutationUiModel {
        return withContext(dispatcher.io) {
            val result = videoPostReminderUseCase.executeOnBackground(channelId, isActive)

            mapper.mapUpdateReminder(result)
        }
    }

    override suspend fun getFeedPosts(
        userID: String,
        cursor: String,
        limit: Int
    ): UserFeedPostsUiModel {
        return withContext(dispatcher.io) {
            return@withContext mapper.mapFeedPosts(
                getUserProfileFeedPostsUseCase.executeOnBackground(
                    userID = userID,
                    cursor = cursor,
                    limit = limit
                )
            )
        }
    }

    override suspend fun getPlayVideo(username: String, cursor: String, isSelfProfile: Boolean): UserPlayVideoUiModel {
        return withContext(dispatcher.io) {
            val response = playVodUseCase.executeOnBackground(
                group = if(isSelfProfile) VAL_FEEDS_ADMIN else  VAL_FEEDS_PROFILE,
                cursor = cursor,
                sourceType = if(isSelfProfile) VAL_SOURCE_BUYER_ADMIN else VAL_SOURCE_BUYER,
                sourceId = username,
            )

            mapper.mapPlayVideo(response)
        }
    }

    override suspend fun getShopRecom(cursor: String): ShopRecomUiModel =
        withContext(dispatcher.io) {
            val result = shopRecomUseCase.executeOnBackground(
                screenName = VAL_SCREEN_NAME_USER_PROFILE,
                limit = VAL_LIMIT,
                cursor = cursor
            )

            return@withContext shopRecomMapper.mapShopRecom(result, VAL_LIMIT)
        }

    override suspend fun getUserProfileTab(userID: String): ProfileTabUiModel {
        return withContext(dispatcher.io) {
            val result = getUserProfileTabUseCase.executeOnBackground(
                userID = userID
            )
            return@withContext mapper.mapProfileTab(result)
        }
    }

    override suspend fun blockUser(userId: String) = withContext(dispatcher.io) {
        val response = postBlockUserUseCase.execute(userId, true)
        if (!response.data.success) error("Failed to block user $userId")
    }

    override suspend fun unblockUser(userId: String) = withContext(dispatcher.io) {
        val response = postBlockUserUseCase.execute(userId, false)
        if (!response.data.success) error("Failed to unblock user $userId")
    }

    override suspend fun deletePlayChannel(
        channelId: String,
        userId: String
    ) = withContext(dispatcher.io) {
        updateChannelUseCase.setQueryParams(
            UpdateChannelUseCase.createUpdateStatusRequest(channelId, userId, PlayChannelStatusType.Deleted)
        )
        updateChannelUseCase.executeOnBackground().id
    }

    override suspend fun getProfileSettings(userID: String): List<ProfileSettingsUiModel> = withContext(dispatcher.io) {
        val response = getProfileSettingsUseCase(
            GetProfileSettingsRequest(
                authorID = userID,
                authorType = ContentCommonUserType.VALUE_TYPE_ID_USER,
            )
        )

        mapper.mapProfileSettings(response)
    }

    override suspend fun setShowReview(
        userID: String,
        settingID: String,
        isShow: Boolean,
    ) = withContext(dispatcher.io) {
        setProfileSettingsUseCase(
            SetProfileSettingsRequest(
                authorID = userID,
                authorType = ContentCommonUserType.VALUE_TYPE_ID_USER,
                data = listOf(
                    SetProfileSettingsRequest.Data(
                        settingID = settingID,
                        enabled = isShow,
                    )
                )
            )
        ).data.success
    }

    override suspend fun getUserReviewList(
        userID: String,
        limit: Int,
        page: Int
    ): UserReviewUiModel = withContext(dispatcher.io) {
        val response = getUserReviewListUseCase(
            GetUserReviewListRequest(
                userID = userID,
                limit = limit,
                page = page,
            )
        )

        mapper.mapUserReviewList(response, page)
    }

    override suspend fun setLikeStatus(
        feedbackID: String,
        isLike: Boolean,
    ): UserReviewUiModel.LikeDislike = withContext(dispatcher.io) {
        val response = setLikeStatusUseCase(
            SetLikeStatusRequest(
                feedbackID = feedbackID,
                isLike = isLike,
            )
        )

        mapper.mapSetLikeStatus(response)
    }

    companion object {
        private const val VAL_FEEDS_PROFILE = "feeds-profile"
        private const val VAL_FEEDS_ADMIN = "feeds-admin"
        private const val VAL_SOURCE_BUYER = "buyer"
        private const val VAL_SOURCE_BUYER_ADMIN = "buyer-admin"
    }
}
