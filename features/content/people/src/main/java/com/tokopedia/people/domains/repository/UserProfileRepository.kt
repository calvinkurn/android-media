package com.tokopedia.people.domains.repository

import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomUiModel
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowAction
import com.tokopedia.people.model.UserPostModel
import com.tokopedia.people.views.uimodel.MutationUiModel
import com.tokopedia.people.views.uimodel.profile.FollowInfoUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileWhitelistUiModel

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

    suspend fun getPlayVideo(
        username: String,
        cursor: String,
    ): UserPostModel

    suspend fun getShopRecom(): ShopRecomUiModel

    suspend fun shopFollowUnfollow(
        shopId: String,
        action: ShopFollowAction
    ): MutationUiModel

}