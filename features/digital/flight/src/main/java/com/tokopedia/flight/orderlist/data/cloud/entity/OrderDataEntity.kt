package com.tokopedia.flight.orderlist.data.cloud.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by alvarisi on 12/6/17.
 */

class OrderDataEntity(
        @SerializedName("data")
        @Expose
        val orders: List<OrderEntity>)
