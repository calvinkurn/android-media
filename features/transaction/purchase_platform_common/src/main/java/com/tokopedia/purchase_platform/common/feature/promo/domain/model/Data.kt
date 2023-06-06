package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("codes")
    val codes: List<String> = emptyList(),

    @SerializedName("voucher_orders")
    val voucherOrders: List<VoucherOrdersItem> = emptyList(),

    @SerializedName("tracking_details")
    val trackingDetails: List<TrackingDetailsItem> = emptyList(),

    @SerializedName("message")
    val message: Message = Message(),

    @SerializedName("additional_info")
    val additionalInfo: AdditionalInfo = AdditionalInfo(),

    @SerializedName("success")
    val success: Boolean = false
)
