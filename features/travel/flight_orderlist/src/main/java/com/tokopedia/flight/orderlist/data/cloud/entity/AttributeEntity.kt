package com.tokopedia.flight.orderlist.data.cloud.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AttributeEntity(
        @SerializedName("create_time")
        @Expose
        val createTime: String = "",
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("status_string")
        @Expose
        val statusFmt: String = "",
        @SerializedName("flight")
        @Expose
        val flight: FlightEntity)
