package com.tokopedia.homenav.mainnav.data.pojo.favoriteshop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopStats(
    @SerializedName("total_product")
    @Expose
    val totalProducts: String? = "",
    @SerializedName("total_etalase")
    @Expose
    val totalEtalase: String? = "",
    @SerializedName("total_sold")
    @Expose
    val totalSold: String? = ""
)
