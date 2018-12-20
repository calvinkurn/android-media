package com.tokopedia.topads.dashboard.data.model

/**
 * Created by zulfikarrahman on 11/4/16.
 */

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TotalAd (

    @SerializedName("total_shop_ad")
    @Expose
    val totalShopAd: Int = 0,

    @SerializedName("total_product_ad")
    @Expose
    val totalProductAd: Int = 0,

    @SerializedName("total_product_group_ad")
    @Expose
    val totalProductGroupAd: Int = 0,

    @SerializedName("total_keyword")
    @Expose
    val totalKeyword: Int = 0
)
