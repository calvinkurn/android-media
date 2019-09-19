package com.tokopedia.topads.dashboard.data.model

/**
 * Created by zulfikarrahman on 11/4/16.
 */

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TotalAd (

    @SerializedName("total_shop_ad_fmt")
    @Expose
    val totalShopAd: Int = 0,

    @SerializedName("total_product_ad_fmt")
    @Expose
    val totalProductAd: Int = 0,

    @SerializedName("total_product_group_ad_fmt")
    @Expose
    val totalProductGroupAd: Int = 0,

    @SerializedName("total_keyword_fmt")
    @Expose
    val totalKeyword: Int = 0
)
