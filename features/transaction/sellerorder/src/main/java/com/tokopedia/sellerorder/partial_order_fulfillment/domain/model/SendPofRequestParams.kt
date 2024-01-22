package com.tokopedia.sellerorder.partial_order_fulfillment.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.isMoreThanZero

data class SendPofRequestParams(
    @SerializedName("order_id")
    @Expose
    val orderId: Long = 0L,
    @SerializedName("pof_detail")
    @Expose
    val pofDetail: List<PofDetail> = listOf()
) {

    fun valid(): Boolean {
        return orderId.isMoreThanZero() && pofDetail.isNotEmpty()
    }

    data class PofDetail(
        @SerializedName("order_dtl_id")
        @Expose
        val orderDetailId: Long = 0,
        @SerializedName("quantity_request")
        @Expose
        val quantityRequest: Int = 0
    )
}
