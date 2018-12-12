package com.tokopedia.profile.data.pojo.profileheader

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Profile(
        @SerializedName("totalFollower")
        @Expose
        val totalFollower: TotalFollower = TotalFollower(),

        @SerializedName("isKol")
        @Expose
        val isKol: Boolean = false,

        @SerializedName("isAffiliate")
        @Expose
        val isAffiliate: Boolean = false,

        @SerializedName("isShowAffiliateContent")
        @Expose
        val isShowAffiliateContent: Boolean = false,

        @SerializedName("totalFollowing")
        @Expose
        val totalFollowing: TotalFollowing = TotalFollowing(),

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("link")
        @Expose
        val link: String = "",

        @SerializedName("affiliateName")
        @Expose
        val affiliateName: String = "",

        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("avatar")
        @Expose
        val avatar: String = "",

        @SerializedName("isFollowed")
        @Expose
        val isFollowed: Boolean = false
)