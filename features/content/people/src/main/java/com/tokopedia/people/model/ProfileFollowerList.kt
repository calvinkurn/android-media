package com.tokopedia.people.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.library.baseadapter.BaseItem

data class ProfileFollowerListBase(
    @SerializedName("feedXProfileFollowerList")
    val profileFollowers: ProfileFollowerList,
)

data class ProfileFollowerList(
    @SerializedName("followers")
    val profileFollower: List<ProfileFollowerV2>,

    @SerializedName("newCursor")
    val newCursor: String,
)

data class ProfileFollowingListBase(
    @SerializedName("feedXProfileFollowingList")
    val profileFollowings: ProfileFollowingList,
)

data class ProfileFollowingList(
    @SerializedName("followings")
    val profileFollower: List<ProfileFollowerV2>,

    @SerializedName("newCursor")
    val newCursor: String,
)

data class ProfileFollowerV2(
    @SerializedName("profile")
    val profile: Profile,

    @SerializedName("isFollow")
    var isFollow: Boolean,
) : BaseItem()
