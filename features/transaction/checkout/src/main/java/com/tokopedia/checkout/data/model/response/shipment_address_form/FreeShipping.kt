package com.tokopedia.checkout.data.model.response.shipment_address_form

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-09-29.
 */

data class FreeShipping(
        @SerializedName("eligible")
        val eligible: Boolean = false,

        @SerializedName("badge_url")
        val badgeUrl: String = ""
)