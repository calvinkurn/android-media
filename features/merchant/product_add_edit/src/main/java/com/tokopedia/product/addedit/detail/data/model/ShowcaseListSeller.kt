package com.tokopedia.product.addedit.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopShowcaseListSellerResponse(
        @SerializedName("shopShowcases")
        @Expose
        val shopShowcases: ShopShowcases = ShopShowcases()
)

data class ShopShowcases(
        @SerializedName("error")
        @Expose
        val error: Error = Error(),
        @SerializedName("result")
        @Expose
        val result: List<ShowcaseItem> = listOf()
)

data class Error(
        @SerializedName("message")
        @Expose
        val message: String = ""
)