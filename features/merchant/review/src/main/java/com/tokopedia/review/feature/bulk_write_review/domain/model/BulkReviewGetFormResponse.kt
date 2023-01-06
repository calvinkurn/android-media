package com.tokopedia.review.feature.bulk_write_review.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BulkReviewGetFormResponse(
    @SerializedName("data")
    @Expose
    val data: Data? = null
) {
    data class Data(
        @SerializedName("productrevGetBulkForm")
        @Expose
        val productrevGetBulkForm: ProductRevGetBulkForm? = null
    ) {
        data class ProductRevGetBulkForm(
            @SerializedName("list")
            @Expose
            val reviewForm: List<ReviewForm>? = null
        ) {
            data class ReviewForm(
                @SerializedName("inboxID")
                @Expose
                val inboxID: String? = null,
                @SerializedName("reputationID")
                @Expose
                val reputationID: String? = null,
                @SerializedName("orderID")
                @Expose
                val orderID: String? = null,
                @SerializedName("product")
                @Expose
                val product: Product? = null,
                @SerializedName("timestamp")
                @Expose
                val timestamp: Timestamp? = null
            ) {
                data class Product(
                    @SerializedName("productID")
                    @Expose
                    val productID: String? = null,
                    @SerializedName("productName")
                    @Expose
                    val productName: String? = null,
                    @SerializedName("productImageURL")
                    @Expose
                    val productImageURL: String? = null,
                    @SerializedName("productVariant")
                    @Expose
                    val productVariant: Variant? = null
                ) {
                    data class Variant(
                        @SerializedName("variantID")
                        @Expose
                        val variantID: String? = null,
                        @SerializedName("variantName")
                        @Expose
                        val variantName: String? = null
                    )
                }

                data class Timestamp(
                    @SerializedName("createTimeFormatted")
                    @Expose
                    val createTimeFormatted: String? = null
                )
            }
        }
    }
}
