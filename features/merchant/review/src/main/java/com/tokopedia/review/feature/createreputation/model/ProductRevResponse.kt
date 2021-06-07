package com.tokopedia.review.feature.createreputation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductRevResponse(
        @SerializedName("productData")
        @Expose
        val productData: ProductData = ProductData(),
        @SerializedName("reputationID")
        @Expose
        val reputationID: Long = 0,
        @SerializedName("orderID")
        @Expose
        val orderID: String = "",
        @SerializedName("validToReview")
        @Expose
        val validToReview: Boolean = true,
        @SerializedName("shopData")
        @Expose
        val shopData: ShopData = ShopData(),
        @SerializedName("userData")
        @Expose
        val userData: UserData = UserData(),
        @SerializedName("reputationData")
        @Expose
        val reputation: Reputation = Reputation()
)