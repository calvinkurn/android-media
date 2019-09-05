package com.tokopedia.affiliate.feature.dashboard.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-09-05.
 */
data class DashboardBalance(
        @SerializedName("buyer_usable")
        @Expose
        val buyerUsable: Long,

        @SerializedName("seller_usable")
        @Expose
        val sellerUsable: Long
)