package com.tokopedia.checkout.data.model.response.shipment_address_form.promo_checkout

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 16/03/20.
 */
data class UsageSummaries (
        @field:SerializedName("description")
        val desc: String? = null,

        @field:SerializedName("type")
        val type: String? = null,

        @field:SerializedName("amount_str")
        val amountStr: String? = null,

        @field:SerializedName("amount")
        val amount: Int? = null
)