package com.tokopedia.profile.following_list.data.pojo.usershopfollow

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-10-22
 */
data class UserShopFollowDetail(
        @SerializedName("shopID")
        @Expose
        val shopID: String = "0",

        @SerializedName("shopName")
        @Expose
        val shopName: String = "",

        @SerializedName("logo")
        @Expose
        val logo: String = "",

        @SerializedName("url")
        @Expose
        val url: String = "",

        @SerializedName("stats")
        @Expose
        val stats: UserShopFollowStats = UserShopFollowStats(),

        @SerializedName("badge")
        @Expose
        val badge: UserShopFollowBadge = UserShopFollowBadge()

)