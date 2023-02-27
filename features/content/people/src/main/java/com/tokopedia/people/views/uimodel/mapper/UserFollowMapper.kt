package com.tokopedia.people.views.uimodel.mapper

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.people.model.ProfileFollowerListBase
import com.tokopedia.people.model.ProfileFollowerV2
import com.tokopedia.people.model.ProfileFollowingListBase
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel
import javax.inject.Inject

class UserFollowMapper @Inject constructor() {

    fun mapMyFollowers(response: ProfileFollowerListBase, myUserId: String): List<ProfileUiModel.PeopleUiModel> {
        return response.profileFollowers.profileFollower.map {
            if (it.profile.encryptedUserID.isNotBlank()) mapUser(it, myUserId)
            else mapShop(it)
        }
    }

    fun mapMyFollowing(response: ProfileFollowingListBase, myUserId: String): List<ProfileUiModel.PeopleUiModel> {
        return response.profileFollowings.profileFollower.map {
            if (it.profile.encryptedUserID.isNotBlank()) mapUser(it, myUserId)
            else mapShop(it)
        }
    }

    private fun mapUser(data: ProfileFollowerV2, myUserId: String): ProfileUiModel.UserUiModel {
        return ProfileUiModel.UserUiModel(
            id = data.profile.userID,
            encryptedId = data.profile.encryptedUserID,
            photoUrl = data.profile.imageCover,
            name = MethodChecker.fromHtml(data.profile.name).toString(),
            username = data.profile.username,
            isFollowed = data.isFollow,
            isMySelf = data.profile.userID == myUserId,
            appLink = data.profile.sharelink.applink
        )
    }

    private fun mapShop(data: ProfileFollowerV2): ProfileUiModel.ShopUiModel {
        val badges = data.profile.badges
        return ProfileUiModel.ShopUiModel(
            id = data.profile.userID,
            logoUrl = data.profile.imageCover,
            badgeUrl = if (badges.isNotEmpty()) {
                if (badges.size > 1) badges[1] else badges[0]
            } else { "" },
            name = MethodChecker.fromHtml(data.profile.name).toString(),
            isFollowed = data.isFollow,
            appLink = data.profile.sharelink.applink
        )
    }
}
