package com.tokopedia.expresscheckout.data.entity.response.profile

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

data class Profile(
        @SerializedName("id")
        val id: Int,

        @SerializedName("status")
        val status: Int,

        @SerializedName("address")
        val address: Address,

        @SerializedName("payment")
        val payment: Payment,

        @SerializedName("shipment")
        val shipment: Shipment
)