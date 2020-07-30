package com.tokopedia.sellerorder.confirmshipping.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-11-20.
 */
data class SomConfirmShippingParam (
        @SerializedName("order_id")
        @Expose
        var orderId: String = "",

        @SerializedName("shipping_ref")
        @Expose
        var shippingRef: String = "",

        @SerializedName("is_active_saldo_prioritas")
        @Expose
        var isActiveSaldoPrioritas: Boolean = false)