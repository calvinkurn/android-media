package com.tokopedia.sellerorder.partial_order_fulfillment.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetPofRequestEstimateResponse(
    @SerializedName("data")
    @Expose
    val `data`: Data? = null
) {
    data class Data(
        @SerializedName("partial_order_fulfillment_request_estimate")
        @Expose
        val partialOrderFulfillmentRequestEstimate: PartialOrderFulfillmentRequestEstimate? = null
    ) {
        data class PartialOrderFulfillmentRequestEstimate(
            @SerializedName("detail_info")
            @Expose
            val detailInfo: List<DetailInfo?>? = null,
            @SerializedName("order_id")
            @Expose
            val orderId: Long? = null,
            @SerializedName("pof_final_estimation")
            @Expose
            val pofFinalEstimation: PofFinalEstimation? = null,
            @SerializedName("pof_info")
            @Expose
            val pofInfo: PofInfo? = null,
            @SerializedName("pof_summary")
            @Expose
            val pofSummary: List<PofSummary?>? = null
        ) {
            data class DetailInfo(
                @SerializedName("order_dtl_id")
                @Expose
                val orderDtlId: Long? = null,
                @SerializedName("product_id")
                @Expose
                val productId: Long? = null,
                @SerializedName("quantity_request")
                @Expose
                val quantityRequest: Int? = null
            )

            data class PofFinalEstimation(
                @SerializedName("label")
                @Expose
                val label: String? = null,
                @SerializedName("tooltip")
                @Expose
                val tooltip: String? = null,
                @SerializedName("value_num")
                @Expose
                val valueNum: Int? = null,
                @SerializedName("value_string")
                @Expose
                val valueString: String? = null
            )

            data class PofInfo(
                @SerializedName("has_info")
                @Expose
                val hasInfo: Boolean? = null,
                @SerializedName("text")
                @Expose
                val text: String? = null
            ) {
                fun hasValidInfo(): Boolean {
                    return hasInfo == true && !text.isNullOrBlank()
                }
            }

            data class PofSummary(
                @SerializedName("label")
                @Expose
                val label: String? = null,
                @SerializedName("value_num")
                @Expose
                val valueNum: Int? = null,
                @SerializedName("value_string")
                @Expose
                val valueString: String? = null
            )
        }
    }
}
