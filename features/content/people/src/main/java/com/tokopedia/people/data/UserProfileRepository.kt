package com.tokopedia.people.data

import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModel
import com.tokopedia.people.model.UserPostModel
import com.tokopedia.people.views.uimodel.content.UserFeedPostsUiModel
import com.tokopedia.people.views.uimodel.profile.FollowInfoUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileTabUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileWhitelistUiModel

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
interface UserProfileRepository {

    suspend fun getProfile(username: String): ProfileUiModel

    suspend fun getFollowInfo(profileIdList: List<String>): FollowInfoUiModel

    suspend fun getWhitelist(): ProfileWhitelistUiModel

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
    ): UserPostModel

    suspend fun getShopRecom(cursor: String): ShopRecomUiModel

    suspend fun getUserProfileTab(userID: String): ProfileTabUiModel

    suspend fun blockUser(userId: String)

    suspend fun unblockUser(userId: String)
}
