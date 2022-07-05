package com.tokopedia.people.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.people.domains.*
import com.tokopedia.people.domains.repository.UserProfileRepository
import com.tokopedia.feedcomponent.domain.usecase.GetWhitelistNewUseCase
import com.tokopedia.feedcomponent.domain.usecase.GetWhitelistUseCase.Companion.WHITELIST_ENTRY_POINT
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

    override suspend fun getWhitelist(userId: String): ProfileWhitelistUiModel {
        return withContext(dispatcher.io) {
            val result = getWhitelistNewUseCase.execute(WHITELIST_ENTRY_POINT, userId)

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

    override suspend fun updateReminder(channelId: String, isActive: Boolean) : MutationUiModel {
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

    companion object {
        private const val VAL_FEEDS_PROFILE = "feeds-profile"
        private const val VAL_SOURCE_BUYER = "buyer"
    }
}