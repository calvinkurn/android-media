package com.tokopedia.profile.data.pojo.profileheader

import com.google.gson.annotations.SerializedName

data class Profile(
        @SerializedName("totalFollower")
        val totalFollower: TotalFollower = TotalFollower(),

        @SerializedName("isKol")
        val isKol: Boolean = false,

        @SerializedName("totalFollowing")
        val totalFollowing: TotalFollowing = TotalFollowing(),

        @SerializedName("name")
        val name: String = "",

        @SerializedName("link")
        val link: String = "",

        @SerializedName("id")
        val id: String = "",

        @SerializedName("avatar")
        val avatar: String = "",

        @SerializedName("isFollowed")
        val isFollowed: Boolean = false
)
