package com.tokopedia.shop.sort.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by nathan on 3/4/18.
 */
class ShopProductSortList {
    @SerializedName("sort")
    @Expose
    var sort: List<ShopProductSort> = ArrayList()
}