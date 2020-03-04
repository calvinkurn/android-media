package com.tokopedia.travelhomepage.homepage.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 2020-03-04
 */

data class TravelLayoutSubhomepage(
        @SerializedName("data")
        @Expose
        val layoutList: List<Data> = listOf(),

        @SerializedName("meta")
        @Expose
        val meta: Meta = Meta()
) {
    data class Data(
            @SerializedName("id")
            @Expose
            val layoutId: String = "",

            @SerializedName("dataType")
            @Expose
            val dataType: String = "",

            @SerializedName("widgetType")
            @Expose
            val widgetType: String = "",

            @SerializedName("priority")
            @Expose
            val priority: String = ""
    )

    data class Meta(
            @SerializedName("templateID")
            @Expose
            val templateId: String = "",

            @SerializedName("templateName")
            @Expose
            val templateName: String = ""
    )

    data class Response(
            @SerializedName("travelLayoutSubhomepage")
            @Expose
            val response: TravelLayoutSubhomepage = TravelLayoutSubhomepage()
    )
}