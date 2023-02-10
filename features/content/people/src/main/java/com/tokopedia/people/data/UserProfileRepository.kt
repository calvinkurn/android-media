package com.tokopedia.people.data

import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowAction
import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModel
import com.tokopedia.people.model.ProfileFollowerListBase
import com.tokopedia.people.model.ProfileFollowingListBase
import com.tokopedia.people.model.UserPostModel
import com.tokopedia.people.views.uimodel.content.UserFeedPostsUiModel
import com.tokopedia.people.views.uimodel.content.UserPlayVideoUiModel
import com.tokopedia.people.views.uimodel.profile.FollowInfoUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileTabUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileWhitelistUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
interface UserProfileRepository {

    suspend fun getProfile(username: String): ProfileUiModel

    suspend fun getFollowInfo(profileIdList: List<String>): FollowInfoUiModel

    suspend fun getWhitelist(): ProfileWhitelistUiModel

    suspend fun followProfile(encryptedUserId: String): MutationUiModel

    suspend fun unFollowProfile(encryptedUserId: String): MutationUiModel

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
    ): UserPlayVideoUiModel

    suspend fun getShopRecom(cursor: String): ShopRecomUiModel

    suspend fun shopFollowUnfollow(
        shopId: String,
        action: ShopFollowAction,
    ): MutationUiModel

    suspend fun getFollowerList(
        username: String,
        cursor: String,
        limit: Int,
    ): ProfileFollowerListBase

    suspend fun getFollowingList(
        username: String,
        cursor: String,
        limit: Int,
    ): ProfileFollowingListBase

    suspend fun getUserProfileTab(userID: String): ProfileTabUiModel

    suspend fun blockUser(userId: String)

    suspend fun unblockUser(userId: String)

    suspend fun deletePlayChannel(
        channel: PlayWidgetChannelUiModel,
        userId: String,
    ): String
}
