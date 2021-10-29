package com.tokopedia.cart.data.model.response.recentview

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 21/09/18.
 */

data class GqlRecentView(
    @SerializedName("items")
    @Expose
    var recentViewList: List<RecentView>? = null
)
