package com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-11-07.
 */

data class Addresses(
        @SerializedName("active")
        val active: String?,

        @SerializedName("data")
        val data: Data?
)