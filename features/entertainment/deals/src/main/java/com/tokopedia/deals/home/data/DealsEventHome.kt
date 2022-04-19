package com.tokopedia.deals.home.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 18/06/20
 */

data class DealsEventHome(
        @SerializedName("layout")
        @Expose
        val layout: List<EventHomeLayout> = listOf(),

        @SerializedName("meta_description")
        @Expose
        val metaDescription: String = "",

        @SerializedName("metaTitle")
        @Expose
        val metaTitle: String = "",

        @SerializedName("ticker")
        @Expose
        val ticker: TickerHome = TickerHome()
) {
    data class Response(
            @SerializedName("event_home")
            @Expose
            val response: DealsEventHome = DealsEventHome()
    )

    data class TickerHome(
            @SerializedName("devices")
            @Expose
            val devices: String = "",

            @SerializedName("message")
            @Expose
            val message: String = ""
    )
}

