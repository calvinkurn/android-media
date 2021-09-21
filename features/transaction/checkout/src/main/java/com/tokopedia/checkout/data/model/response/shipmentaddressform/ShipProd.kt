package com.tokopedia.checkout.data.model.response.shipmentaddressform

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class ShipProd(
        @SuppressLint("Invalid Data Type")
        @SerializedName("ship_prod_id")
        val shipProdId: Int = 0,
        @SerializedName("ship_prod_name")
        val shipProdName: String = "",
        @SerializedName("ship_group_name")
        val shipGroupName: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("ship_group_id")
        val shipGroupId: Int = 0,
        @SerializedName("additional_fee")
        val additionalFee: Int = 0,
        @SerializedName("minimum_weight")
        val minimumWeight: Int = 0
)

