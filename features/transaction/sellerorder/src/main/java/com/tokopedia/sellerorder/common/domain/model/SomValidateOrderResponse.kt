package com.tokopedia.sellerorder.common.domain.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomValidateOrderResponse(@SerializedName("data")
                                          @Expose
                                          val data: Data) {
    data class Data(@SerializedName("validate_accept_order")
                    @Expose
                    val result: ValidateAcceptOrder) {
        data class ValidateAcceptOrder(@SuppressLint("Invalid Data Type")
                                       @SerializedName("list_order_id")
                                       @Expose
                                       val orderIds: List<String>)
    }
}