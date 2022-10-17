package com.tokopedia.people.views.uimodel.mapper

import com.tokopedia.people.views.uimodel.profile.*
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.people.model.ProfileHeaderBase
import com.tokopedia.people.model.UserProfileIsFollow
import com.tokopedia.people.model.VideoPostReimderModel

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
interface UserProfileUiMapper {
    fun mapUserProfile(response: ProfileHeaderBase): ProfileUiModel

    fun mapFollowInfo(response: UserProfileIsFollow): FollowInfoUiModel

    fun mapUserWhitelist(response: WhitelistQuery): ProfileWhitelistUiModel

    fun mapUpdateReminder(response: VideoPostReimderModel): MutationUiModel
}
