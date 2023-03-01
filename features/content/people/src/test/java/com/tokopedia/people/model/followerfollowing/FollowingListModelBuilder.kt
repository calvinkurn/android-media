package com.tokopedia.people.model.followerfollowing

import com.tokopedia.people.views.uimodel.profile.ProfileUiModel

/**
 * Created By : Jonathan Darwin on July 06, 2022
 */
class FollowingListModelBuilder {

    fun build(cursor: String, size: Int): Pair<List<ProfileUiModel.PeopleUiModel>, String> {
        return Pair(
            List(size) {
                ProfileUiModel.UserUiModel(
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
            cursor
        )
    }
}
