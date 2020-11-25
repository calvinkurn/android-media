package com.tokopedia.cart.data.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UndoDeleteCartRequest(
    @SerializedName("cart_ids")
    @Expose
    var cartIds: List<String> = emptyList()
)
