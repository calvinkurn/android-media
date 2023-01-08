package com.tokopedia.people.model.followerfollowing

import com.tokopedia.people.model.*

/**
 * Created By : Jonathan Darwin on July 06, 2022
 */
class FollowingListModelBuilder {

    fun buildModel(
        size: Int = 5,
        isFollow: Boolean = false,
        cursor: String = "",
    ): ProfileFollowingListBase {
        return ProfileFollowingListBase(
            profileFollowings = ProfileFollowingList(
                profileFollower = List(size) {
                    ProfileFollowerV2(
                        profile = Profile(
                            userID = it.toString(),
                            encryptedUserID = it.toString(),
                            imageCover = "https://tokopedia.com/image/$it",
                            name = "Follower $it",
                            username = "username$it",
                            biography = "Bio $it",
                            sharelink = Link(
                                applink = "applink $it",
                                weblink = "weblink $it",
                            ),
                            badges = emptyList(),
                            liveplaychannel = Liveplaychannel(
                                islive = false,
                                liveplaychannelid = it.toString(),
                                liveplaychannellink = Link(
                                    applink = "applink live $it",
                                    weblink = "weblink live $it",
                                ),
                            ),
                        ),
                        isFollow = isFollow,
                    )
                },
                newCursor = cursor,
            ),
        )
    }
}
