package com.tokopedia.promocheckout.common.domain.model.event

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data(
        @SerializedName("cart")
        @Expose
        val cart: Cart = Cart(),
        @SerializedName("status")
        @Expose
        val status: Status = Status(),
        @SerializedName("message")
        @Expose
        val message: String = ""
)