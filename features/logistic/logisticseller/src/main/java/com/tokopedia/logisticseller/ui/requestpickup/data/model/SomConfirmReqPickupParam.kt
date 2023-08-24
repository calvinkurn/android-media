package com.tokopedia.logisticseller.ui.requestpickup.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-11-13.
 */
data class SomConfirmReqPickupParam (
        @SerializedName("order_list")
        @Expose
        var orderList: ArrayList<Order> = arrayListOf()) {

    data class Order(
            @SerializedName("order_id")
            @Expose
            var orderId: String = "",

            @SerializedName("awbnum")
            @Expose
            var awbnum: String = "")
}
