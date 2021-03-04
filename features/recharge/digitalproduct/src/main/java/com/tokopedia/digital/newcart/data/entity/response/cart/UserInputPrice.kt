package com.tokopedia.digital.newcart.data.entity.response.cart

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 2/27/17.
 */

class UserInputPrice {

    @SerializedName("min_payment")
    @Expose
    var minPayment: String? = null
    @SerializedName("max_payment")
    @Expose
    var maxPayment: String? = null
    @SerializedName("min_payment_plain")
    @Expose
    var minPaymentPlain: Long = 0
    @SerializedName("max_payment_plain")
    @Expose
    var maxPaymentPlain: Long = 0
}
