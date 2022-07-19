package com.tokopedia.people.listener

interface FollowerListenerTracker {
    fun clickUserFollowers(userId: String, self: Boolean)
    fun clickUnfollowFromFollowers(userId: String, self: Boolean)
    fun clickFollowFromFollowers(userId: String, self: Boolean)
}