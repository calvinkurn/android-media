package com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 09/03/20.
 */
data class ErrorDefault (
        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("Description")
        val description: String? = null)