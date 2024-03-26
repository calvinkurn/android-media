package com.tokopedia.people.repo

import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowAction
import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.people.data.UserFollowRepository
import com.tokopedia.people.views.uimodel.FollowListUiModel

class MockUserFollowRepository : UserFollowRepository {

    private val followersMap = mutableMapOf<GetDataArgs, FollowListUiModel.Follower>()
    private val followingMap = mutableMapOf<GetDataArgs, FollowListUiModel.Following>()

    override suspend fun followShop(shopId: String, action: ShopFollowAction): MutationUiModel {
        TODO()
    }

    override suspend fun followUser(encryptedUserId: String, follow: Boolean): MutationUiModel {
        TODO()
    }

    override suspend fun getFollowers(
        username: String,
        cursor: String,
        limit: Int
    ): FollowListUiModel.Follower {
        val args = GetDataArgs(username, cursor)
        return followersMap[args] ?: error("Followers Data for $username not found")
    }

    override suspend fun getFollowing(
        username: String,
        cursor: String,
        limit: Int
    ): FollowListUiModel.Following {
        val args = GetDataArgs(username, cursor)
        return followingMap[args] ?: error("Following Data for $username not found")
    }

    fun setFollowersData(
        username: String,
        cursor: String,
        result: FollowListUiModel.Follower
    ) {
        val args = GetDataArgs(username, cursor)
        followersMap[args] = result
    }

    fun setFollowingData(
        username: String,
        cursor: String,
        result: FollowListUiModel.Following
    ) {
        val args = GetDataArgs(username, cursor)
        followingMap[args] = result
    }

    fun clear() {
        followersMap.clear()
        followingMap.clear()
    }

    private data class GetDataArgs(
        val userName: String,
        val cursor: String = ""
    )
}
