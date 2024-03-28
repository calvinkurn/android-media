package com.tokopedia.people.repo

import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowAction
import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.people.data.UserFollowRepository
import com.tokopedia.people.views.uimodel.FollowListUiModel

class MockUserFollowRepository : UserFollowRepository {

    private val followersMap = mutableMapOf<GetDataArgs, FollowListUiModel.Follower>()
    private val followingMap = mutableMapOf<GetDataArgs, FollowListUiModel.Following>()

    private var mErrorFollowShop: String? = null
    private var mErrorFollowUser: String? = null

    override suspend fun followShop(shopId: String, action: ShopFollowAction): MutationUiModel {
        return mErrorFollowShop?.let {
            MutationUiModel.Error(it)
        } ?: MutationUiModel.Success()
    }

    override suspend fun followUser(encryptedUserId: String, follow: Boolean): MutationUiModel {
        return mErrorFollowUser?.let {
            MutationUiModel.Error(it)
        } ?: MutationUiModel.Success()
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

    fun setErrorFollowShop(
        errorMessage: String? = null
    ) {
        mErrorFollowShop = errorMessage
    }

    fun setErrorFollowUser(
        errorMessage: String? = null
    ) {
        mErrorFollowUser = errorMessage
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

        mErrorFollowShop = null
        mErrorFollowUser = null
    }

    private data class GetDataArgs(
        val userName: String,
        val cursor: String = ""
    )
}
