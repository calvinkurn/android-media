package com.tokopedia.checkout.bundle.data.model.response.shipmentaddressform

import android.annotation.SuppressLint
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

        @SuppressLint("Invalid Data Type")
        @SerializedName("price")
        val price: Long = 0,

        @SerializedName("info")
        val info: CrossSellInfoData = CrossSellInfoData(),

        @SerializedName("order_summary")
        val orderSummary: CrossSellOrderSummary = CrossSellOrderSummary(),

        @SerializedName("bottom_sheet")
        val bottomSheet: CrossSellBottomSheet = CrossSellBottomSheet(),

        @SuppressLint("Invalid Data Type")
        @SerializedName("additional_vertical_id")
        val additionalVerticalId: Long = 0,

        @SerializedName("transaction_type")
        val transactionType: String = "")
