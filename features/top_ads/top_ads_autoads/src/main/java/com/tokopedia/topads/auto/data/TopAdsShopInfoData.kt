package com.tokopedia.topads.auto.data

import com.google.gson.annotations.SerializedName

/**
 * Author errysuprayogi on 15,May,2019
 */
class TopAdsShopInfoData {

    @SerializedName("category")
    var category: Int = 0
    @SerializedName("category_desc")
    var categoryDesc: String = ""
}
