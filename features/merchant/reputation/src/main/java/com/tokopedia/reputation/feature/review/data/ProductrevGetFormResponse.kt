package com.tokopedia.reputation.feature.review.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetFormResponse(
        @SerializedName("productData")
        @Expose
        val productData: ProductrevGetFormProductData = ProductrevGetFormProductData(),
        @SerializedName("userData")
        @Expose
        val userData: ProductrevGetFormUserData = ProductrevGetFormUserData(),
        @SerializedName("review")
        @Expose
        val review: ProductrevGetFormReview = ProductrevGetFormReview(),
        @SerializedName("response")
        @Expose
        val response: ProductrevGetFormSellerResponse = ProductrevGetFormSellerResponse(),
        @SerializedName("reputation")
        @Expose
        val reputation: ProductrevGetFormReputation = ProductrevGetFormReputation()
)