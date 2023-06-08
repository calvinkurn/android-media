package com.tokopedia.people.views.uimodel

/**
 * Created by meyta.taliti on 07/03/23.
 */
class FollowListUiModel {

    data class Follower(
        val total: FollowCount,
        val followers: List<PeopleUiModel>,
        val nextCursor: String
    )

    data class Following(
        val total: FollowCount,
        val followingList: List<PeopleUiModel>,
        val nextCursor: String
    )

    data class FollowCount(
        val totalFollowers: String,
        val totalFollowing: String,
    )

    companion object {
        private val emptyFollowCount = FollowCount(
            "",
            ""
        )

        val emptyFollowers = Follower(
            total = emptyFollowCount,
            followers = emptyList(),
            nextCursor = ""
        )

        val emptyFollowingList = Following(
            total = emptyFollowCount,
            followingList = emptyList(),
            nextCursor = ""
        )
    }
}
