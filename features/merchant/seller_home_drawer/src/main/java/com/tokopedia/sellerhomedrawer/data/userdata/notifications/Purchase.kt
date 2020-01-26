package com.tokopedia.sellerhomedrawer.data.userdata.notifications

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Purchase (

    @SerializedName("reorder")
    @Expose
    var purchaseReorder: Int = 0,
    @SerializedName("paymentConfirm")
    @Expose
    var purchasePaymentConfirm: Int = 0,
    @SerializedName("orderStatus")
    @Expose
    var purchaseOrderStatus: Int = 0,
    @SerializedName("deliveryConfirm")
    @Expose
    var purchaseDeliveryConfirm: Int = 0

)