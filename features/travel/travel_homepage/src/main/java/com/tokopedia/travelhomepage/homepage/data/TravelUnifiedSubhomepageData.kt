package com.tokopedia.travelhomepage.homepage.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 2020-03-03
 */

data class TravelUnifiedSubhomepageData(
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("product")
        @Expose
        val product: String = "",

        @SerializedName("promoCode")
        @Expose
        val promoCode: String = "",

        @SerializedName("description")
        @Expose
        val description: String = "",

        @SerializedName("cityID")
        @Expose
        val cityID: String = "",

        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("subtitle")
        @Expose
        val subtitle: String = "",

        @SerializedName("prefix")
        @Expose
        val prefix: String = "",

        @SerializedName("prefixStyle")
        @Expose
        val prefixStyle: String = "",

        @SerializedName("value")
        @Expose
        val value: String = "",

        @SerializedName("imageURL")
        @Expose
        val imageUrl: String = "",

        @SerializedName("webURL")
        @Expose
        val webUrl: String = "",

        @SerializedName("appURL")
        @Expose
        val appUrl: String = ""
) {
    open class Response(
            @SerializedName("TravelGetDynamicSubhomepage")
            @Expose
            val response: List<TravelUnifiedSubhomepageData> = listOf()
    )
}