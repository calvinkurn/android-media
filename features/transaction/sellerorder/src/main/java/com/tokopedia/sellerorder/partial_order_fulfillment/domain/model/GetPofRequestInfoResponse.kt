package com.tokopedia.sellerorder.partial_order_fulfillment.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetPofRequestInfoResponse(
    @SerializedName("data")
    @Expose
    val `data`: Data? = null
) {
    data class Data(
        @SerializedName("info_request_partial_order_fulfillment")
        @Expose
        val infoRequestPartialOrderFulfillment: InfoRequestPartialOrderFulfillment? = null
    ) {
        data class InfoRequestPartialOrderFulfillment(
            @SerializedName("details_fulfilled")
            @Expose
            val detailsFulfilled: List<Details?>? = null,
            @SerializedName("details_original")
            @Expose
            val detailsOriginal: List<Details?>? = null,
            @SerializedName("details_unfulfilled")
            @Expose
            val detailsUnfulfilled: List<Details?>? = null,
            @SerializedName("order_id")
            @Expose
            val orderId: Long? = null,
            @SerializedName("pof_status")
            @Expose
            val pofStatus: Int? = null
        ) {
            companion object {
                const val STATUS_INVALID = -1
                const val STATUS_INITIAL = 0
                const val STATUS_CANCELLED = 1
                const val STATUS_REJECTED = 3
                const val STATUS_IGNORED = 4
                const val STATUS_WAITING_RESPONSE = 101
                const val STATUS_APPROVED = 200
            }
        }

        data class Details(
            @SerializedName("order_dtl_id")
            @Expose
            val orderDetailId: Long? = null,
            @SerializedName("order_id")
            @Expose
            val orderId: Long? = null,
            @SerializedName("product_id")
            @Expose
            val productId: Long? = null,
            @SerializedName("product_name")
            @Expose
            val productName: String? = null,
            @SerializedName("product_picture")
            @Expose
            val productPicture: String? = null,
            @SerializedName("product_price")
            @Expose
            val productPrice: String? = null,
            @SerializedName("product_quantity")
            @Expose
            val productQuantity: Int? = null,
            @SerializedName("quantity_checkout")
            @Expose
            val quantityCheckout: Int? = null,
            @SerializedName("quantity_request")
            @Expose
            val quantityRequest: Int? = null,
            @SerializedName("total_price_checkout")
            @Expose
            val totalPriceCheckout: Int? = null,
            @SerializedName("total_price_request")
            @Expose
            val totalPriceRequest: Int? = null
        )
    }
}
