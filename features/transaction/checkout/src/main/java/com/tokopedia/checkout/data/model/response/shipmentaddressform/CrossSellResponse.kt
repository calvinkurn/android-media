package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 26/10/21.
 */
data class CrossSellResponse(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("checkbox_disabled")
    val checkboxDisabled: Boolean = false,

    @SerializedName("is_checked")
    val isChecked: Boolean = false,

    @SerializedName("price")
    val price: Double = 0.0,

    @SerializedName("info")
    val info: CrossSellInfoData = CrossSellInfoData(),

    @SerializedName("order_summary")
    val orderSummary: CrossSellOrderSummary = CrossSellOrderSummary(),

    @SerializedName("bottom_sheet")
    val bottomSheet: CrossSellBottomSheet = CrossSellBottomSheet(),

    @SerializedName("additional_vertical_id")
    val additionalVerticalId: String = "",

    @SerializedName("transaction_type")
    val transactionType: String = ""
)
