package com.tokopedia.createpost.uprofile.model

import com.google.gson.annotations.SerializedName

data class ProfileHeaderBase (
    @SerializedName("feedXProfileHeader")
    val profileHeader: FeedXProfileHeader
)

data class FeedXProfileHeader (
    val profile: Profile,
    val stats: Stats,
    val hasAcceptTnC: Boolean,
    val shouldSeoIndex: Boolean
)

data class Profile (
    val userID: String,
    val encryptedUserID: String,
    val imageCover: String,
    val name: String,
    val username: String,
    val biography: String,
    val sharelink: Link,
    val badges: List<Any?>,
    val liveplaychannel: Liveplaychannel
)

data class Liveplaychannel (
    val islive: Boolean,
    val liveplaychannelid: String,
    val liveplaychannellink: Link
)

data class Link (
    val applink: String,
    val weblink: String
)

data class Stats (
    val totalPost: Long,
    val totalPostFmt: String,
    val totalFollower: Long,
    val totalFollowerFmt: String,
    val totalFollowing: Long,
    val totalFollowingFmt: String
)

