package com.tokopedia.people.listener

interface FollowFollowingTracker {
    fun clickUnfollowFromFollowing(userId: String, self: Boolean)
    fun clickFollowFromFollowing(userId: String, self: Boolean)
    fun clickUserFollowing(userId: String, self: Boolean)
    fun clickUserFollowers(userId: String, self: Boolean)
    fun clickUnfollowFromFollowers(userId: String, self: Boolean)
    fun clickFollowFromFollowers(userId: String, self: Boolean)
}