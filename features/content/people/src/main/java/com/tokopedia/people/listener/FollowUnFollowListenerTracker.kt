package com.tokopedia.people.listener

interface FollowUnFollowListenerTracker {
    fun clickUser(userId: String, self: Boolean)
    fun clickUnfollow(userId: String, self: Boolean)
    fun clickFollow(userId: String, self: Boolean)
}