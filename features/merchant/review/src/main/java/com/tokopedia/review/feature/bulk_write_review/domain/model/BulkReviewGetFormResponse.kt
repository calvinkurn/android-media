package com.tokopedia.review.feature.bulk_write_review.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BulkReviewGetFormResponse(
    @SerializedName("data")
    @Expose
    val data: Data = Data()
) {
    data class Data(
        @SerializedName("productrevGetBulkForm")
        @Expose
        val productrevGetBulkForm: ProductRevGetBulkForm = ProductRevGetBulkForm()
    ) {
        data class ProductRevGetBulkForm(
            @SerializedName("themeCopy")
            @Expose
            val themeCopy: String = "",
            @SerializedName("list")
            @Expose
            val reviewForm: List<ReviewForm> = emptyList()
        ) {
            data class ReviewForm(
                @SerializedName("inboxID")
                @Expose
                val inboxID: String = "",
                @SerializedName("reputationID")
                @Expose
                val reputationID: String = "",
                @SerializedName("orderID")
                @Expose
                val orderID: String = "",
                @SerializedName("product")
                @Expose
                val product: Product = Product(),
                @SerializedName("timestamp")
                @Expose
                val timestamp: Timestamp = Timestamp()
            ) {
                data class Product(
                    @SerializedName("productID")
                    @Expose
                    val productID: String = "",
                    @SerializedName("productName")
                    @Expose
                    val productName: String = "",
                    @SerializedName("productImageURL")
                    @Expose
                    val productImageURL: String = "",
                    @SerializedName("productVariant")
                    @Expose
                    val productVariant: Variant = Variant()
                ) {
                    data class Variant(
                        @SerializedName("variantID")
                        @Expose
                        val variantID: String = "",
                        @SerializedName("variantName")
                        @Expose
                        val variantName: String = ""
                    )
                }

                data class Timestamp(
                    @SerializedName("createTimeFormatted")
                    @Expose
                    val createTimeFormatted: String = ""
                )
            }
        }
    }
}
