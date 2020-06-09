package com.tokopedia.one.click.checkout.order.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UpdateCartOccRequest(
        @SerializedName("cart")
        @Expose
        val cart: ArrayList<UpdateCartOccCartRequest> = ArrayList(),
        @SerializedName("profile")
        @Expose
        val profile: UpdateCartOccProfileRequest = UpdateCartOccProfileRequest()
)