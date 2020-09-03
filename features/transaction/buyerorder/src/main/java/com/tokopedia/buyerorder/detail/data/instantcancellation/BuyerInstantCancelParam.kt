package com.tokopedia.buyerorder.detail.data.instantcancellation

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 28/07/20.
 */
data class BuyerInstantCancelParam (
        @SerializedName("order_id")
        val orderId: String = "",

        @SerializedName("lang")
        val lang: String = "id",

        @SerializedName("reason_code")
        val reasonCode: String = "",

        @SerializedName("reason")
        val reason: String = ""
)