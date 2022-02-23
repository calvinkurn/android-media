package com.tokopedia.createpost.uprofile.model

import com.google.gson.annotations.SerializedName

data class UserProfileIsFollow(
    @SerializedName("feedXProfileIsFollowing")
    val profileHeader: ProfileUserFollowing
)

data class ProfileUserFollowing(
    @SerializedName("isUserFollowing")
    val items: MutableList<FollowingProfile>
)

data class ProfileIsFollowing(
    @SerializedName("feedXProfileIsFollowing")
    val items: MutableList<FollowingProfile>
)

data class FollowingProfile(
    @SerializedName("userID")
    val userID: String,

    @SerializedName("encryptedUserID")
    val encryptedUserID: String,

    @SerializedName("status")
    val status: Boolean
)