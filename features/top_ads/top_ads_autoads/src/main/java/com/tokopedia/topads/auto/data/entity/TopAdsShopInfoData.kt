package com.tokopedia.topads.auto.data.entity

import com.google.gson.annotations.SerializedName

/**
 * Author errysuprayogi on 15,May,2019
 */
data class TopAdsShopInfoData (

    @SerializedName("category")
    val category: Int = 0,
    @SerializedName("category_desc")
    val categoryDesc: String = ""
)
