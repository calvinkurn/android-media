package com.tokopedia.common_digital.cart.data.entity.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 3/9/17.
 */

class ResponseCheckoutData {
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("id")
    @Expose
    var id: Int = 0
    @SerializedName("attributes")
    @Expose
    var attributes: AttributesCheckout? = null
}
