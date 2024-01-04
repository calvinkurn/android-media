package com.tokopedia.order_management_common.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ProductBenefit(
    @SerializedName("icon_url")
    @Expose
    val iconUrl: String = "",
    @SerializedName("label")
    @Expose
    val label: String = "",
    @SerializedName("order_detail")
    @Expose
    val orderDetail: List<OrderDetail>? = listOf()
) {

    fun isValid(): Boolean {
        return iconUrl.isNotBlank() && label.isNotBlank() && !orderDetail.isNullOrEmpty()
    }

    data class OrderDetail(
        @SerializedName("order_detail_id")
        @Expose
        val orderDetailId: Long = 0,
        @SerializedName(value = "product_id", alternate = ["id"])
        @Expose
        val productId: Long = 0,
        @SerializedName(value = "product_name", alternate = ["name"])
        @Expose
        val productName: String = "",
        @SerializedName("thumbnail")
        @Expose
        val thumbnail: String = "",
        @SerializedName(value = "total_price_text", alternate = ["price_text"])
        @Expose
        val totalPriceText: String = "",
        @SerializedName("quantity")
        @Expose
        val quantity: Int = 0
    )
}
