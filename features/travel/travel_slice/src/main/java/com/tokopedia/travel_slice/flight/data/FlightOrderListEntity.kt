package com.tokopedia.travel_slice.flight.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 26/11/2020
 */
data class FlightOrderListEntity(
        @SerializedName("metaData")
        @Expose
        val metaData: MetaData = MetaData(),
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("categoryName")
        @Expose
        val categoryName: String = "",
        @SerializedName("statusStr")
        @Expose
        val statusStr: String = "",
        @SerializedName("id")
        @Expose
        val id: String = ""
) {
    class Response(@SerializedName("orders")
                   @Expose
                   val orders: List<FlightOrderListEntity> = arrayListOf())
}

data class MetaData(
        @SerializedName("label")
        @Expose
        val label: String = "",
        @SerializedName("value")
        @Expose
        val value: String = ""
)