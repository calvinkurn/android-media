package com.tokopedia.feedplus.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/08/22
 */
data class FeedSource(
    @SerializedName("origin")
    @Expose val origin: Int = 0,

    @SerializedName("shop")
    @Expose val shop: ShopDetail = ShopDetail(),

    @SerializedName("type")
    @Expose val type: Int = 0
)
