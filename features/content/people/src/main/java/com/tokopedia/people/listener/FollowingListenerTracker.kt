package com.tokopedia.people.listener

interface FollowingListenerTracker {
    fun clickUserFollowing(userId: String, self: Boolean)
    fun clickUnfollowFromFollowing(userId: String, self: Boolean)
    fun clickFollowFromFollowing(userId: String, self: Boolean)
}