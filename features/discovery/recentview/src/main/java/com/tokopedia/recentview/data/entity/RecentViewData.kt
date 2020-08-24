package com.tokopedia.recentview.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RecentViewData {
    @SerializedName("items")
    @Expose
    var list: List<ProductItem> = mutableListOf()

    override fun toString(): String {
        return "ProductItemData{" +
                "list=" + list +
                '}'
    }
}