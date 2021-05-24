package com.tokopedia.sellerorder.requestpickup.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-11-14.
 */
data class SomProcessReqPickupParam (
        @SerializedName("order_id")
        @Expose
        var orderId: String = "",

        @SerializedName("external_id")
        @Expose
        var externalId: String = "",

        @SerializedName("order_variant")
        @Expose
        var orderVariant: Int = 1,

        @SerializedName("is_active_saldo_prioritas")
        @Expose
        var isActiveSaldoPrioritas: Boolean = false,

        @SerializedName("schedule_pickup_time")
        @Expose
        var schedulePickupTime: String = "")