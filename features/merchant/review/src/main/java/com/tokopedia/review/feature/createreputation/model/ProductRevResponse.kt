package com.tokopedia.review.feature.createreputation.model

import com.google.gson.annotations.SerializedName

data class ProductRevResponse(
        @SerializedName("productData")
        val productData: ProductData = ProductData(),
        @SerializedName("reputationID")
        val reputationID: Int = 0,
        @SerializedName("orderID")
        val orderID: String = "",
        @SerializedName("validToReview")
        val validToReview: Boolean = true,
        @SerializedName("shopData")
        val shopData: ShopData = ShopData(),
        @SerializedName("userData")
        val userData: UserData = UserData(),
        @SerializedName("reputationData")
        val reputation: Reputation = Reputation()
)