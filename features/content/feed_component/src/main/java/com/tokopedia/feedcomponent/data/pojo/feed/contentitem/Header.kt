package com.tokopedia.feedcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.SerializedName

data class Header(
        @SerializedName("avatar")
        val avatar: String = "",
        @SerializedName("avatarApplink")
        val avatarApplink: String = "",
        @SerializedName("avatarBadgeImage")
        val avatarBadgeImage: String = "",
        @SerializedName("avatarDate")
        var avatarDate: String = "",
        @SerializedName("avatarDescription")
        val avatarDescription: String = "",
        @SerializedName("avatarTitle")
        val avatarTitle: String = "",
        @SerializedName("avatarWeblink")
        val avatarWeblink: String = "",
        @SerializedName("deletable")
        val deletable: Boolean = false,
        @SerializedName("editable")
        val editable: Boolean = false,
        @SerializedName("followCta")
        val followCta: FollowCta = FollowCta(),
        @SerializedName("reportable")
        val reportable: Boolean = false
)