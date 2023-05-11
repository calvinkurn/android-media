package com.tokopedia.people.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.content.common.usecase.GetWhiteListNewUseCase
import com.tokopedia.content.common.usecase.GetWhiteListNewUseCase.Companion.WHITELIST_ENTRY_POINT
import com.tokopedia.feedcomponent.domain.usecase.GetUserProfileFeedPostsUseCase
import com.tokopedia.feedcomponent.domain.usecase.shoprecom.ShopRecomUseCase
import com.tokopedia.feedcomponent.domain.usecase.shoprecom.ShopRecomUseCase.Companion.VAL_LIMIT
import com.tokopedia.feedcomponent.domain.usecase.shoprecom.ShopRecomUseCase.Companion.VAL_SCREEN_NAME_USER_PROFILE
import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.feedcomponent.shoprecom.mapper.ShopRecomUiMapper
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModel
import com.tokopedia.people.domains.*
import com.tokopedia.people.model.SetProfileSettingsRequest
import com.tokopedia.people.views.uimodel.ProfileSettingsUiModel
import com.tokopedia.people.views.uimodel.content.UserFeedPostsUiModel
import com.tokopedia.people.views.uimodel.content.UserPlayVideoUiModel
import com.tokopedia.people.views.uimodel.mapper.UserProfileUiMapper
import com.tokopedia.people.views.uimodel.profile.FollowInfoUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileTabUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileWhitelistUiModel
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
    private val getWhitelistNewUseCase: GetWhiteListNewUseCase,
    private val shopRecomUseCase: ShopRecomUseCase,
    private val getUserProfileTabUseCase: GetUserProfileTabUseCase,
    private val getUserProfileFeedPostsUseCase: GetUserProfileFeedPostsUseCase,
    private val postBlockUserUseCase: PostBlockUserUseCase,
    private val updateChannelUseCase: UpdateChannelUseCase,
    private val setProfileSettingsUseCase: SetProfileSettingsUseCase,
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

    override suspend fun getWhitelist(): ProfileWhitelistUiModel {
        return withContext(dispatcher.io) {
            val result = getWhitelistNewUseCase.execute(WHITELIST_ENTRY_POINT)

            mapper.mapUserWhitelist(result)
        }
    }

    override suspend fun updateReminder(channelId: String, isActive: Boolean): MutationUiModel {
        return withContext(dispatcher.io) {
            val result = videoPostReminderUseCase.executeOnBackground(channelId, isActive)

            mapper.mapUpdateReminder(result)
        }
    }

    override suspend fun getFeedPosts(userID: String, cursor: String, limit: Int): UserFeedPostsUiModel {
        return withContext(dispatcher.io) {
            return@withContext mapper.mapFeedPosts(
                getUserProfileFeedPostsUseCase.executeOnBackground(
                    userID = userID,
                    cursor = cursor,
                    limit = limit,
                ),
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

    override suspend fun getShopRecom(cursor: String): ShopRecomUiModel = withContext(dispatcher.io) {
        val result = shopRecomUseCase.executeOnBackground(
            screenName = VAL_SCREEN_NAME_USER_PROFILE,
            limit = VAL_LIMIT,
            cursor = cursor,
        )

        return@withContext shopRecomMapper.mapShopRecom(result, VAL_LIMIT)
    }

    override suspend fun getUserProfileTab(userID: String): ProfileTabUiModel {
        return withContext(dispatcher.io) {
            val result = getUserProfileTabUseCase.executeOnBackground(
                userID = userID,
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

    override suspend fun setShowReview(
        userID: String,
        isShow: Boolean,
    ) = withContext(dispatcher.io) {
        setProfileSettingsUseCase(
            SetProfileSettingsRequest(
                authorID = userID,
                authorType = ContentCommonUserType.TYPE_USER,
                data = listOf(
                    SetProfileSettingsRequest.Data(
                        settingID = ProfileSettingsUiModel.KEY_REVIEWS,
                        enable = isShow,
                    )
                )
            )
        ).data.success
    }

    companion object {
        private const val VAL_FEEDS_PROFILE = "feeds-profile"
        private const val VAL_FEEDS_ADMIN = "feeds-admin"
        private const val VAL_SOURCE_BUYER = "buyer"
        private const val VAL_SOURCE_BUYER_ADMIN = "buyer-admin"
    }
}
