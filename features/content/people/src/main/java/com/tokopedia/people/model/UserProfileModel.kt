package com.tokopedia.people.model

import com.google.gson.annotations.SerializedName

data class ProfileHeaderBase(
    @SerializedName("feedXProfileHeader")
    val profileHeader: FeedXProfileHeader,
)

data class FeedXProfileHeader(
    @SerializedName("profile")
    val profile: Profile,

    @SerializedName("stats")
    val stats: Stats,

    @SerializedName("hasAcceptTnC")
    val hasAcceptTnC: Boolean,

    @SerializedName("shouldSeoIndex")
    val shouldSeoIndex: Boolean,
)

data class Profile(
    @SerializedName("userID")
    val userID: String,

    @SerializedName("encryptedUserID")
    val encryptedUserID: String,

    @SerializedName("imageCover")
    val imageCover: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("biography")
    var biography: String,

    @SerializedName("sharelink")
    val sharelink: Link,

    @SerializedName("badges")
    val badges: List<String>,

    @SerializedName("liveplaychannel")
    val liveplaychannel: Liveplaychannel,
)

data class Liveplaychannel(
    @SerializedName("islive")
    val islive: Boolean,

    @SerializedName("liveplaychannelid")
    val liveplaychannelid: String,

    @SerializedName("liveplaychannellink")
    val liveplaychannellink: Link,
)

data class Link(
    @SerializedName("applink")
    val applink: String,

    @SerializedName("weblink")
    val weblink: String,
)

data class Stats(
    @SerializedName("totalPost")
    val totalPost: Long,

    @SerializedName("totalPostFmt")
    val totalPostFmt: String,

    @SerializedName("totalFollower")
    val totalFollower: Long,

    @SerializedName("totalFollowerFmt")
    val totalFollowerFmt: String,

    @SerializedName("totalFollowing")
    val totalFollowing: Long,

    @SerializedName("totalFollowingFmt")
    val totalFollowingFmt: String,
)
