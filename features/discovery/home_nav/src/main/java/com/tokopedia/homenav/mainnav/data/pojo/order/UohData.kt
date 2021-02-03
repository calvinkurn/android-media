package com.tokopedia.homenav.mainnav.data.pojo.order

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UohData(
        @SerializedName("uohOrders")
        @Expose
        val uohOrders: UohOrders? = UohOrders()
)