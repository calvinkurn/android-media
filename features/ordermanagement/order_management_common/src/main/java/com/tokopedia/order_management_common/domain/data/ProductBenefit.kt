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
    val orderDetail: List<OrderDetail> = listOf()
) {
    data class OrderDetail(
        @SerializedName("order_dtl_id")
        @Expose
        val orderDtlId: Long = 0,
        @SerializedName("product_id")
        @Expose
        val productId: Long = 0,
        @SerializedName("product_name")
        @Expose
        val productName: String = "",
        @SerializedName("picture")
        @Expose
        val picture: String = "",
        @SerializedName("product_price")
        @Expose
        val productPrice: String = "",
        @SerializedName("product_qty")
        @Expose
        val productQty: Int = 0
    )
}
