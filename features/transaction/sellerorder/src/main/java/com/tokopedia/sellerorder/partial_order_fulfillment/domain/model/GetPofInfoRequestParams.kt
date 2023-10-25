package com.tokopedia.sellerorder.partial_order_fulfillment.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.isMoreThanZero

data class GetPofInfoRequestParams(
    @SerializedName("order_id")
    @Expose
    val orderId: Long = 0,
    @SerializedName("delay")
    @Expose(serialize = false, deserialize = false)
    val delay: Long? = null
) {
    fun valid(): Boolean {
        return orderId.isMoreThanZero()
    }
}
