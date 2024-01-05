package com.tokopedia.deals.ui.home.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 18/06/20
 */

data class DealsEventHome(
    @SerializedName("layout")
        @Expose
        val layout: List<com.tokopedia.deals.ui.home.data.EventHomeLayout> = listOf(),

    @SerializedName("meta_description")
        @Expose
        val metaDescription: String = "",

    @SerializedName("metaTitle")
        @Expose
        val metaTitle: String = "",

    @SerializedName("ticker")
        @Expose
        val ticker: com.tokopedia.deals.ui.home.data.DealsEventHome.TickerHome = com.tokopedia.deals.ui.home.data.DealsEventHome.TickerHome()
) {
    data class Response(
            @SerializedName("event_home")
            @Expose
            val response: com.tokopedia.deals.ui.home.data.DealsEventHome = com.tokopedia.deals.ui.home.data.DealsEventHome()
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

