package com.tokopedia.people.views.uimodel.mapper

import com.tokopedia.people.views.uimodel.profile.*
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.people.model.*
import com.tokopedia.people.views.uimodel.MutationUiModel
import com.tokopedia.people.views.uimodel.shoprecom.ShopRecomUiModel

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
interface UserProfileUiMapper {
    fun mapUserProfile(response: ProfileHeaderBase): ProfileUiModel

    fun mapFollowInfo(response: UserProfileIsFollow): FollowInfoUiModel

    fun mapUserWhitelist(response: WhitelistQuery): ProfileWhitelistUiModel

    fun mapFollow(response: ProfileDoFollowModelBase): MutationUiModel

    fun mapUnfollow(response: ProfileDoUnFollowModelBase): MutationUiModel

    fun mapUpdateReminder(response: VideoPostReimderModel): MutationUiModel

    fun mapShopRecom(response: UserShopRecomModel): ShopRecomUiModel
}