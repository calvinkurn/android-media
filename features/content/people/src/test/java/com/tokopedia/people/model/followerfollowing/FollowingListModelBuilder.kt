package com.tokopedia.people.model.followerfollowing

import com.tokopedia.people.views.uimodel.FollowListUiModel
import com.tokopedia.people.views.uimodel.PeopleUiModel

/**
 * Created By : Jonathan Darwin on July 06, 2022
 */
class FollowingListModelBuilder {

    fun build(cursor: String, size: Int): FollowListUiModel.Following {
        return FollowListUiModel.Following(
            total = FollowListUiModel.FollowCount("", ""),
            followingList = List(size) {
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
