package com.tokopedia.shop.common.graphql.data.shopsort

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class ShopProductSortList {
    @SerializedName("sort")
    @Expose
    var sort: List<ShopProductSort> = ArrayList()
}
