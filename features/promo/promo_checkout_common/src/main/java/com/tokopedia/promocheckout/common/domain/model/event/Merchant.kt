package com.tokopedia.promocheckout.common.domain.model.event

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Merchant(
        @SerializedName("address")
        @Expose
        val address: Any = Any(),
        @SerializedName("id")
        @Expose
        val id: Int = 0
)