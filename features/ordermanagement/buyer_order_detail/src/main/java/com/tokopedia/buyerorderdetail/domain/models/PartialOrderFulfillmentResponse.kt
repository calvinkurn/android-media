package com.tokopedia.buyerorderdetail.domain.models


import com.google.gson.annotations.SerializedName

data class PartialOrderFulfillmentResponse(
    @SerializedName("info_respond_partial_order_fulfillment")
    val infoRespondPartialOrderFulfillment: InfoRespondPartialOrderFulfillment = InfoRespondPartialOrderFulfillment()
)

data class InfoRespondPartialOrderFulfillment(
    @SerializedName("details_fulfilled")
    val detailsFulfilled: List<DetailsFulfilled> = listOf(),
    @SerializedName("details_unfulfill")
    val detailsUnfulfill: List<DetailsUnfulfill> = listOf(),
    @SerializedName("estimate_info")
    val estimateInfo: EstimateInfo = EstimateInfo(),
    @SerializedName("footer_info")
    val footerInfo: String = "",
    @SerializedName("header_info")
    val headerInfo: String = "",
    @SerializedName("order_id")
    val orderId: Long = 0,
    @SerializedName("summary")
    val summary: Summary = Summary(),
    @SerializedName("header_fulfilled")
    val headerFulfilled: String = ""
)

data class DetailsFulfilled(
    @SerializedName("order_dtl_id")
    val orderDtlId: Long = 0,
    @SerializedName("product_id")
    val productId: Long = 0,
    @SerializedName("product_name")
    val productName: String = "",
    @SerializedName("product_picture")
    val productPicture: String = "",
    @SerializedName("product_price")
    val productPrice: String = "",
    @SerializedName("product_quantity")
    val productQuantity: Long = 0
)

data class DetailsUnfulfill(
    @SerializedName("order_dtl_id")
    val orderDtlId: Long = 0,
    @SerializedName("product_id")
    val productId: Long = 0,
    @SerializedName("product_name")
    val productName: String = "",
    @SerializedName("product_picture")
    val productPicture: String = "",
    @SerializedName("product_price")
    val productPrice: String = "",
    @SerializedName("product_quantity_checkout")
    val productQuantityCheckout: Long = 0,
    @SerializedName("product_quantity_request")
    val productQuantityRequest: Long = 0
)

data class EstimateInfo(
    @SerializedName("info")
    val info: String = "",
    @SerializedName("title")
    val title: String = ""
)

data class EstimateRefund(
    @SerializedName("key")
    val key: String = "",
    @SerializedName("label")
    val label: String = "",
    @SerializedName("value")
    val value: String = ""
)

data class Info(
    @SerializedName("info")
    val info: String = ""
)

data class PofDetail(
    @SerializedName("key")
    val key: String = "",
    @SerializedName("label")
    val label: String = "",
    @SerializedName("value")
    val value: String = ""
)

data class Summary(
    @SerializedName("estimate_refund")
    val estimateRefund: EstimateRefund = EstimateRefund(),
    @SerializedName("pof_details")
    val pofDetails: List<PofDetail> = listOf()
)
