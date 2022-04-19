package com.tokopedia.review.feature.inboxreview.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InboxReviewResponse(
        @Expose
        @SerializedName("productrevGetInboxReviewByShop")
        val productrevGetInboxReviewByShop: ProductGetInboxReviewByShop = ProductGetInboxReviewByShop()
) {
    data class ProductGetInboxReviewByShop(
            @Expose
            @SerializedName("filterBy")
            val filterBy: String? = "",
            @Expose
            @SerializedName("hasNext")
            val hasNext: Boolean? = false,
            @Expose
            @SerializedName("limit")
            val limit: Int? = 0,
            @Expose
            @SerializedName("list")
            val list: List<InboxReviewList> = listOf(),
            @Expose
            @SerializedName("page")
            val page: Int? = 0,
            @Expose
            @SerializedName("remainder")
            val remainder: Int? = 0,
            @Expose
            @SerializedName("reviewCount")
            val reviewCount: Int? = 0,
            @Expose
            @SerializedName("useAutoReply")
            val useAutoReply: Boolean? = false
    ) {
        data class InboxReviewList (
                @Expose
                @SerializedName("attachments")
                val attachments: List<Attachment> = listOf(),
                @Expose
                @SerializedName("feedbackIDStr")
                val feedbackID: String = "",
                @Expose
                @SerializedName("invoiceID")
                val invoiceID: String? = "",
                @Expose
                @SerializedName("isAutoReply")
                val isAutoReply: Boolean? = false,
                @Expose
                @SerializedName("product")
                val product: Product = Product(),
                @Expose
                @SerializedName("rating")
                val rating: Int? = 0,
                @Expose
                @SerializedName("replyText")
                val replyText: String? = "",
                @Expose
                @SerializedName("replyTime")
                val replyTime: String? = "",
                @Expose
                @SerializedName("reviewText")
                val reviewText: String? = "",
                @Expose
                @SerializedName("reviewTime")
                val reviewTime: String? = "",
                @Expose
                @SerializedName("user")
                val user: User = User(),
                @SerializedName("isKejarUlasan")
                @Expose
                val isKejarUlasan: Boolean = false,
                @SerializedName("badRatingReasonFmt")
                @Expose
                val badRatingReasonFmt: String = "",
                @SerializedName("ratingDisclaimer")
                @Expose
                val ratingDisclaimer: String = ""
        ) {
            data class Attachment(
                    @Expose
                    @SerializedName("fullsizeURL")
                    val fullSizeURL: String? = "",
                    @Expose
                    @SerializedName("thumbnailURL")
                    val thumbnailURL: String? = ""
            )
            data class Product(
                    @Expose
                    @SerializedName("productIDStr")
                    val productID: String = "",
                    @Expose
                    @SerializedName("productName")
                    val productName: String? = "",
                    @Expose
                    @SerializedName("productImageURL")
                    val productImageURL: String? = "",
                    @Expose
                    @SerializedName("productPageURL")
                    val productPageURL: String? = "",
                    @Expose
                    @SerializedName("productVariant")
                    val productVariant: ProductVariant = ProductVariant()
            ) {
                data class ProductVariant(
                        @Expose
                        @SerializedName("variantIDStr")
                        val variantID: String = "",
                        @Expose
                        @SerializedName("variantName")
                        val variantName: String? = ""
                )
            }
            data class User(
                    @Expose
                    @SerializedName("userIDStr")
                    val userID: String = "",
                    @Expose
                    @SerializedName("userName")
                    val userName: String? = ""
            )
        }
    }
}