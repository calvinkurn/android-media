package com.tokopedia.sellerorder.partial_order_fulfillment.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.isMoreThanZero

data class GetPofEstimateRequestParams(
    @SerializedName("detail_info")
    @Expose
    val detailInfo: List<DetailInfo?>? = null,
    @SerializedName("order_id")
    @Expose
    val orderId: Long? = null,
    @SerializedName("delay")
    @Expose(serialize = false, deserialize = false)
    val delay: Long? = null
) {
    fun valid(): Boolean {
        return detailInfo?.filterNotNull()?.isNotEmpty() == true && orderId.isMoreThanZero()
    }

    data class DetailInfo(
        @SerializedName("order_dtl_id")
        @Expose
        val orderDetailId: Long? = null,
        @SerializedName("product_id")
        @Expose
        val productId: Long? = null,
        @SerializedName("quantity_request")
        @Expose
        val quantityRequest: Int? = null
    )
}
