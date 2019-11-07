package com.tokopedia.kol.feature.following_list.data.pojo.usershopfollow

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-10-22
 */
data class UserShopFollowStats(
        @SerializedName("totalShowcase")
        @Expose
        val totalShowcase: String = "0",

        @SerializedName("productSold")
        @Expose
        val productSold: String = "0"
)