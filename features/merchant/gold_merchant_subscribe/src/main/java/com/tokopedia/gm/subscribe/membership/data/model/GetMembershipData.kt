package com.tokopedia.gm.subscribe.membership.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetMembershipData(
        @SerializedName("shop_id")
        @Expose
        var shop_id: Int = 0,
        @SerializedName("expired_date")
        @Expose
        var expired_date: String = "",
        @SerializedName("auto_withdrawal_date")
        @Expose
        var auto_withdrawal_date: String = "",
        @SerializedName("auto_extend")
        @Expose
        var auto_extend: Int = 0,
        @SerializedName("subscription")
        @Expose
        var subscription: Subscription = Subscription()
)