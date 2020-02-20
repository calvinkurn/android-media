package com.tokopedia.profile.following_list.data.pojo.usershopfollow

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-10-22
 */
data class UserShopFollowResult(
        @SerializedName("userShopFollowDetail")
        @Expose
        val userShopFollowDetail: List<UserShopFollowDetail> = emptyList(),

        @SerializedName("haveNext")
        @Expose
        val haveNext: Boolean = false,

        @SerializedName("totalCount")
        @Expose
        val totalCount: String = "0"
)