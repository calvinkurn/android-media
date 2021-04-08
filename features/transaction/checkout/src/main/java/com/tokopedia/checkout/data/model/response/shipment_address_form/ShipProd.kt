package com.tokopedia.checkout.data.model.response.shipment_address_form

import com.google.gson.annotations.SerializedName

data class ShipProd(
        @SerializedName("ship_prod_id")
        val shipProdId: Int = 0,
        @SerializedName("ship_prod_name")
        val shipProdName: String = "",
        @SerializedName("ship_group_name")
        val shipGroupName: String = "",
        @SerializedName("ship_group_id")
        val shipGroupId: Int = 0,
        @SerializedName("additional_fee")
        val additionalFee: Int = 0,
        @SerializedName("minimum_weight")
        val minimumWeight: Int = 0
)

