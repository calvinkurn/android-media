package com.tokopedia.people.listener

interface FollowingFollowerListener {
    fun clickUser(userId: String, self: Boolean)
    fun clickUnfollow(userId: String, self: Boolean)
    fun clickFollow(userId: String, self: Boolean)
}
