package com.tokopedia.sellerorder.confirmshipping.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-11-14.
 */
data class SomChangeCourierParam (
        @SerializedName("order_id")
        @Expose
        var orderId: String = "",

        @SerializedName("shipping_ref")
        @Expose
        var shippingRef: String = "",

        @SerializedName("agency_id")
        @Expose
        var agencyId: Int = -1,

        @SerializedName("sp_id")
        @Expose
        var spId: Int = -1)