package com.tokopedia.travel_slice.hotel.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.travel_slice.flight.data.Item

/**
 * @author by jessica on 25/11/20
 */

data class HotelOrderListModel(
        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("statusStr")
        @Expose
        val statusStr: String = "",

        @SerializedName("appLink")
        @Expose
        val applink: String = "",

        @SerializedName("items")
        @Expose
        val items: List<Item> = arrayListOf()
) {
    data class Response(
            @SerializedName("orders")
            @Expose
            val response: List<HotelOrderListModel> = listOf()
    )
}

data class HotelOrderListParams (
        @SerializedName("orderCategory")
        @Expose
        val orderCategory: String = "HOTELS",

        @SerializedName("Page")
        @Expose
        val page: Int = 1,

        @SerializedName("PerPage")
        @Expose
        val perPage: Int = 3
)

data class Item(
        @SerializedName("imageUrl")
        @Expose
        val imageUrl: String = ""
)