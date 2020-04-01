package com.tokopedia.profile.following_list.data.pojo.usershopfollow

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-10-22
 */
data class UserShopFollowBadge(
        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("imageURL")
        @Expose
        val imageUrl: String = ""
)