package com.tokopedia.checkout.data.model.response.shipment_address_form

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductTicker {
    @SerializedName("show_ticker")
    @Expose
    var isShowTicker = false
    @SerializedName("message")
    @Expose
    var message: String? = null
}