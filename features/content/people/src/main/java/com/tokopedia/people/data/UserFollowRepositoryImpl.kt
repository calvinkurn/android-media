package com.tokopedia.people.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowAction
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowUseCase
import com.tokopedia.feedcomponent.people.mapper.ProfileMutationMapper
import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.feedcomponent.people.usecase.ProfileFollowUseCase
import com.tokopedia.feedcomponent.people.usecase.ProfileUnfollowedUseCase
import com.tokopedia.feedcomponent.shoprecom.mapper.ShopRecomUiMapper
import com.tokopedia.people.domains.GetFollowerListUseCase
import com.tokopedia.people.domains.GetFollowingListUseCase
import com.tokopedia.people.views.uimodel.mapper.UserFollowMapper
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserFollowRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val shopFollowUseCase: ShopFollowUseCase,
    private val getFollowerListUseCase: GetFollowerListUseCase,
    private val getFollowingListUseCase: GetFollowingListUseCase,
    private val doFollowUseCase: ProfileFollowUseCase,
    private val doUnfollowUseCase: ProfileUnfollowedUseCase,
    private val shopMapper: ShopRecomUiMapper,
    private val userMapper: ProfileMutationMapper,
    private val userFollowMapper: UserFollowMapper,
    private val userSession: UserSessionInterface
) : UserFollowRepository {

    override suspend fun followShop(shopId: String, action: ShopFollowAction): MutationUiModel {
        return withContext(dispatcher.io) {
            val result = shopFollowUseCase(
                shopFollowUseCase.createParams(
                    shopId = shopId,
                    action = action
                )
            )
            shopMapper.mapShopFollow(result)
        }
    }

    override suspend fun followUser(encryptedUserId: String, follow: Boolean): MutationUiModel {
        return withContext(dispatcher.io) {
            if (follow) {
                val result = doFollowUseCase.executeOnBackground(encryptedUserId)
                userMapper.mapFollow(result)
            } else {
                val result = doUnfollowUseCase.executeOnBackground(encryptedUserId)
                userMapper.mapUnfollow(result)
            }
        }
    }

    override suspend fun getMyFollowers(
        username: String,
        cursor: String,
        limit: Int
    ): Pair<List<ProfileUiModel.PeopleUiModel>, String> {
        return withContext(dispatcher.io) {
            val response = getFollowerListUseCase.executeOnBackground(
                username = username,
                cursor = cursor,
                limit = limit
            )
            Pair(
                userFollowMapper.mapMyFollowers(response, userSession.userId),
                response.profileFollowers.newCursor
            )
        }
    }

    override suspend fun getMyFollowing(
        username: String,
        cursor: String,
        limit: Int
    ): Pair<List<ProfileUiModel.PeopleUiModel>, String> {
        return withContext(dispatcher.io) {
            val response = getFollowingListUseCase.executeOnBackground(
                username = username,
                cursor = cursor,
                limit = limit
            )
            Pair(
                userFollowMapper.mapMyFollowing(response, userSession.userId),
                response.profileFollowings.newCursor
            )
        }
    }
}
