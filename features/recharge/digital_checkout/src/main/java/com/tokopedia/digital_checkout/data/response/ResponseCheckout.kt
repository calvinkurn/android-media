package com.tokopedia.digital_checkout.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common_digital.cart.data.entity.response.AttributesCheckout

/**
 * @author by jessica on 21/01/21
 */

data class ResponseCheckout(
        @SerializedName("type")
        @Expose
        var type: String? = null,

        @SerializedName("id")
        @Expose
        var id: Int = 0,

        @SerializedName("attributes")
        @Expose
        var attributes: AttributesCheckout? = null
)