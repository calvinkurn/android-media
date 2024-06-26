package com.tokopedia.review.feature.createreputation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProductRevResponse(
    @SerializedName("productData")
    @Expose
    val productData: ProductData = ProductData(),
    @SerializedName("reputationIDStr")
    @Expose
    val reputationIDStr: String = "",
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
    val reputation: Reputation = Reputation(),
    @SerializedName("keywords")
    @Expose
    val keywords: List<String>? = null,
    @SerializedName("placeholder")
    @Expose
    val placeholder: String? = null
) : Serializable