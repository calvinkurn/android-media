package com.tokopedia.feedcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.SerializedName

data class FollowCta(
        @SerializedName("authorID")
        val authorID: String = "",
        @SerializedName("authorType")
        val authorType: String = "",
        @SerializedName("isFollow")
        var isFollow: Boolean = false,
        @SerializedName("textFalse")
        val textFalse: String = "",
        @SerializedName("textTrue")
        val textTrue: String = ""
) {
    companion object {
        const val AUTHOR_USER = "user"
        const val AUTHOR_SHOP = "shop"
    }
}