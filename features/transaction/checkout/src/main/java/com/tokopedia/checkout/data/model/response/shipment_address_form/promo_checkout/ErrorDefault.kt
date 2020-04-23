package com.tokopedia.checkout.data.model.response.shipment_address_form.promo_checkout

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 09/03/20.
 */
data class ErrorDefault (
        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("description")
        val description: String? = null)