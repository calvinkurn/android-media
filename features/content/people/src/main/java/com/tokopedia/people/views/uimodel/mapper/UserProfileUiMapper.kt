package com.tokopedia.people.views.uimodel.mapper

import com.tokopedia.content.common.model.GetCheckWhitelist
import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.people.model.ProfileHeaderBase
import com.tokopedia.people.model.UserProfileIsFollow
import com.tokopedia.people.model.VideoPostReimderModel
import com.tokopedia.people.views.uimodel.profile.*

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
interface UserProfileUiMapper {
    fun mapUserProfile(response: ProfileHeaderBase): ProfileUiModel

    fun mapFollowInfo(response: UserProfileIsFollow): FollowInfoUiModel

    fun mapUserWhitelist(response: GetCheckWhitelist): ProfileWhitelistUiModel

    fun mapUpdateReminder(response: VideoPostReimderModel): MutationUiModel
}
