package com.tokopedia.people.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomUiModel
import com.tokopedia.feedcomponent.domain.usecase.GetWhitelistNewUseCase
import com.tokopedia.feedcomponent.domain.usecase.WHITELIST_ENTRY_POINT
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowAction
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowUseCase
import com.tokopedia.feedcomponent.domain.usecase.shoprecom.ShopRecomUseCase
import com.tokopedia.people.domains.UserDetailsUseCase
import com.tokopedia.people.domains.PlayPostContentUseCase
import com.tokopedia.people.domains.ProfileFollowUseCase
import com.tokopedia.people.domains.ProfileUnfollowedUseCase
import com.tokopedia.people.domains.ProfileTheyFollowedUseCase
import com.tokopedia.people.domains.VideoPostReminderUseCase
import com.tokopedia.people.domains.repository.UserProfileRepository
import com.tokopedia.people.model.ProfileHeaderBase
import com.tokopedia.people.model.UserPostModel
import com.tokopedia.people.views.uimodel.MutationUiModel
import com.tokopedia.people.views.uimodel.mapper.UserProfileUiMapper
import com.tokopedia.people.views.uimodel.profile.FollowInfoUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileWhitelistUiModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
class UserProfileRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val mapper: UserProfileUiMapper,
    private val userDetailsUseCase: UserDetailsUseCase,
    private val playVodUseCase: PlayPostContentUseCase,
    private val useCaseDoFollow: ProfileFollowUseCase,
    private val useCaseDoUnFollow: ProfileUnfollowedUseCase,
    private val profileIsFollowing: ProfileTheyFollowedUseCase,
    private val videoPostReminderUseCase: VideoPostReminderUseCase,
    private val getWhitelistNewUseCase: GetWhitelistNewUseCase,
    private val shopRecomUseCase: ShopRecomUseCase,
    private val shopFollowUseCase: ShopFollowUseCase,
) : UserProfileRepository {

    override suspend fun getProfile(username: String): ProfileUiModel {
        return withContext(dispatcher.io) {
            val result = userDetailsUseCase.getUserProfileDetail(username)

            mapper.mapUserProfile(result.getData(ProfileHeaderBase::class.java))
        }
    }

    override suspend fun getFollowInfo(profileIdList: List<String>): FollowInfoUiModel {
        return withContext(dispatcher.io) {
            val result = profileIsFollowing.profileIsFollowing(profileIdList)

            mapper.mapFollowInfo(result)
        }
    }

    override suspend fun getWhitelist(): ProfileWhitelistUiModel {
        return withContext(dispatcher.io) {
            val result = getWhitelistNewUseCase.execute(WHITELIST_ENTRY_POINT)

            mapper.mapUserWhitelist(result)
        }
    }

    override suspend fun followProfile(encryptedUserId: String): MutationUiModel {
        return withContext(dispatcher.io) {
            val result = useCaseDoFollow.doFollow(encryptedUserId)

            mapper.mapFollow(result)
        }
    }

    override suspend fun unFollowProfile(encryptedUserId: String): MutationUiModel {
        return withContext(dispatcher.io) {
            val result = useCaseDoUnFollow.doUnfollow(encryptedUserId)

            mapper.mapUnfollow(result)
        }
    }

    override suspend fun updateReminder(channelId: String, isActive: Boolean): MutationUiModel {
        return withContext(dispatcher.io) {
            val result = videoPostReminderUseCase.updateReminder(channelId, isActive)

            mapper.mapUpdateReminder(result)
        }
    }

    override suspend fun getPlayVideo(username: String, cursor: String): UserPostModel {
        return withContext(dispatcher.io) {
            val data = playVodUseCase.getPlayPost(VAL_FEEDS_PROFILE, cursor, VAL_SOURCE_BUYER, username)

            data
        }
    }

    override suspend fun getShopRecom(): ShopRecomUiModel = withContext(dispatcher.io) {
        val result = shopRecomUseCase.apply {
            setRequestParams(ShopRecomUseCase.createParam())
        }.executeOnBackground()
        return@withContext mapper.mapShopRecom(result)
    }

    override suspend fun shopFollowUnfollow(
        shopId: String,
        action: ShopFollowAction
    ): MutationUiModel = withContext(dispatcher.io) {
        val result = shopFollowUseCase.apply {
            setRequestParams(ShopFollowUseCase.createParam(shopId, action))
        }.executeOnBackground()
        return@withContext mapper.mapShopFollow(result)
    }

    companion object {
        private const val VAL_FEEDS_PROFILE = "feeds-profile"
        private const val VAL_SOURCE_BUYER = "buyer"
    }
}