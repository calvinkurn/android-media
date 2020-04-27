package com.tokopedia.common_digital.cart.data.entity.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 3/23/17.
 */

class ResponseInstantCheckoutData {
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("id")
    @Expose
    var id: Int = 0
    @SerializedName("attributes")
    @Expose
    var attributes: AttributesInstantCheckout? = null
}
