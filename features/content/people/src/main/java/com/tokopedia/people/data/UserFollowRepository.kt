package com.tokopedia.people.data

import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowAction
import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel

interface UserFollowRepository {

    suspend fun followShop(shopId: String, action: ShopFollowAction): MutationUiModel

    suspend fun followUser(encryptedUserId: String, follow: Boolean): MutationUiModel

    suspend fun getMyFollowers(
        username: String,
        cursor: String,
        limit: Int,
    ): Pair<List<ProfileUiModel.PeopleUiModel>, String> // with cursor

    suspend fun getMyFollowing(
        username: String,
        cursor: String,
        limit: Int,
    ): Pair<List<ProfileUiModel.PeopleUiModel>, String> // with cursor

}
