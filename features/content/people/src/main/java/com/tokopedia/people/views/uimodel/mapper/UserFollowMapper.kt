package com.tokopedia.people.views.uimodel.mapper

import android.webkit.URLUtil
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.people.model.ProfileFollowerListBase
import com.tokopedia.people.model.ProfileFollowerV2
import com.tokopedia.people.model.ProfileFollowingListBase
import com.tokopedia.people.model.Stats
import com.tokopedia.people.views.uimodel.FollowListUiModel
import com.tokopedia.people.views.uimodel.PeopleUiModel
import javax.inject.Inject

class UserFollowMapper @Inject constructor() {

    fun mapMyFollowers(response: ProfileFollowerListBase, myUserId: String): FollowListUiModel.Follower {
        return FollowListUiModel.Follower(
            total = mapFollowCount(response.profileHeader.stats),
            followers = response.profileFollowers.profileFollower.map {
                if (it.profile.encryptedUserID.isNotBlank()) {
                    mapUser(it, myUserId)
                } else {
                    mapShop(it)
                }
            },
            nextCursor = response.profileFollowers.newCursor
        )
    }

    fun mapMyFollowing(response: ProfileFollowingListBase, myUserId: String): FollowListUiModel.Following {
        return FollowListUiModel.Following(
            total = mapFollowCount(response.profileHeader.stats),
            followingList = response.profileFollowings.profileFollower.map {
                if (it.profile.encryptedUserID.isNotBlank()) {
                    mapUser(it, myUserId)
                } else {
                    mapShop(it)
                }
            },
            nextCursor = response.profileFollowings.newCursor
        )
    }

    private fun mapFollowCount(response: Stats) = FollowListUiModel.FollowCount(
        totalFollowing = response.totalFollowingFmt,
        totalFollowers = response.totalFollowerFmt
    )

    private fun mapUser(data: ProfileFollowerV2, myUserId: String): PeopleUiModel.UserUiModel {
        return PeopleUiModel.UserUiModel(
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

    private fun mapShop(data: ProfileFollowerV2): PeopleUiModel.ShopUiModel {
        return PeopleUiModel.ShopUiModel(
            id = data.profile.userID,
            logoUrl = data.profile.imageCover,
            badgeUrl = getShopBadgeUrl(data.profile.badges).orEmpty(),
            name = MethodChecker.fromHtml(data.profile.name).toString(),
            isFollowed = data.isFollow,
            appLink = data.profile.sharelink.applink
        )
    }

    /**
     * "badges": [
     *   "Official Store",
     *   "https://images.tokopedia.net/img/official_store/badge_os.png"
     *  ]
     */
    private fun getShopBadgeUrl(badgesData: List<String>): String? {
        return badgesData.firstOrNull { URLUtil.isNetworkUrl(it) }
    }
}
