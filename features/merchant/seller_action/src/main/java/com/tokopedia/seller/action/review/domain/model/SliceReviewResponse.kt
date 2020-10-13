package com.tokopedia.seller.action.review.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.seller.action.common.presentation.model.SellerSuccessItem

data class SliceReviewResponse(
        @Expose
        @SerializedName("productrevGetInboxReviewByShop")
        val productrevGetInboxReviewByShop: ProductGetInboxReviewByShop = ProductGetInboxReviewByShop()
)

data class ProductGetInboxReviewByShop(
        @Expose
        @SerializedName("list")
        val list: List<InboxReviewList> = listOf()
)

data class InboxReviewList (
        @Expose
        @SerializedName("feedbackID")
        val feedbackID: Int? = 0,
        @Expose
        @SerializedName("reviewText")
        val reviewText: String? = "",
        @Expose
        @SerializedName("product")
        val product: Product = Product(),
        @Expose
        @SerializedName("rating")
        val rating: Int? = 0,
        @Expose
        @SerializedName("user")
        val user: User = User()
): SellerSuccessItem

data class Product(
        @Expose
        @SerializedName("productName")
        val productName: String? = ""
)

data class User(
        @Expose
        @SerializedName("userName")
        val userName: String? = ""
)