package com.tokopedia.review.feature.inbox.buyerreview.data.pojo

import com.google.gson.annotations.SerializedName
import java.util.*

class FavoriteCheckResult {
    @SerializedName("data")
    var shopIds: List<String> = ArrayList()
    fun getShopIds(): List<String>? {
        return shopIds
    }
}