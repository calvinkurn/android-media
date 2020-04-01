package com.tokopedia.sellerhomedrawer.data.userdata.notifications

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Sales (

    @SerializedName("newOrder")
    @Expose
    var salesNewOrder: Int = 0,
    @SerializedName("shippingStatus")
    @Expose
    var salesShippingStatus: Int = 0,
    @SerializedName("shippingConfirm")
    @Expose
    var salesShippingConfirm: Int = 0

)