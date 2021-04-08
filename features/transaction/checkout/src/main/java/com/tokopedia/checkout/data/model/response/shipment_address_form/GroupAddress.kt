package com.tokopedia.checkout.data.model.response.shipment_address_form

import com.google.gson.annotations.SerializedName

data class GroupAddress(
        @SerializedName("errors")
        val errors: List<String> = emptyList(),
        @SerializedName("user_address")
        val userAddress: UserAddress = UserAddress(),
        @SerializedName("group_shop")
        val groupShop: List<GroupShop> = emptyList()
)