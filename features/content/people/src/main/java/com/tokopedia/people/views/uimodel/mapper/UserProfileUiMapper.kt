package com.tokopedia.people.views.uimodel.mapper

import com.tokopedia.content.common.model.GetCheckWhitelistResponse
import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.people.model.ProfileHeaderBase
import com.tokopedia.feedcomponent.domain.model.UserFeedPostsModel
import com.tokopedia.people.model.UserProfileIsFollow
import com.tokopedia.people.model.UserProfileTabModel
import com.tokopedia.people.model.VideoPostReimderModel
import com.tokopedia.people.views.uimodel.content.UserFeedPostsUiModel
import com.tokopedia.people.views.uimodel.profile.*

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
interface UserProfileUiMapper {
    fun mapUserProfile(response: ProfileHeaderBase): ProfileUiModel

    fun mapFollowInfo(response: UserProfileIsFollow): FollowInfoUiModel

    fun mapUserWhitelist(response: GetCheckWhitelistResponse): ProfileWhitelistUiModel

    fun mapUpdateReminder(response: VideoPostReimderModel): MutationUiModel

    fun mapProfileTab(response: UserProfileTabModel): ProfileTabUiModel

    fun mapFeedPosts(response: UserFeedPostsModel): UserFeedPostsUiModel
}
