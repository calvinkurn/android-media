package com.tokopedia.people.model.followerfollowing

import com.tokopedia.people.views.uimodel.FollowListUiModel
import com.tokopedia.people.views.uimodel.PeopleUiModel

/**
 * Created By : Jonathan Darwin on July 06, 2022
 */
class FollowerListModelBuilder {

    fun build(
        cursor: String = "",
        size: Int = 0,
        followCount: FollowListUiModel.FollowCount = FollowListUiModel.FollowCount("", "")
    ): FollowListUiModel.Follower {
        return FollowListUiModel.Follower(
            total = followCount,
            followers = List(size) {
                PeopleUiModel.UserUiModel(
                    id = it.toString(),
                    encryptedId = "",
                    photoUrl = "",
                    name = "",
                    username = "",
                    isFollowed = false,
                    isMySelf = false,
                    appLink = ""
                )
            },
            nextCursor = cursor
        )
    }
}
