package com.tokopedia.salam.umrah.common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 15/10/2019
 */
class MyUmrahEntity(
        @SerializedName("subHeader")
        @Expose
        val subHeader: String = "",
        @SerializedName("header")
        @Expose
        val header: String = "",
        @SerializedName("nextActionText")
        @Expose
        val nextActionText: String = "",
        @SerializedName("mainButton")
        @Expose
        val mainButton: MainButton = MainButton(),
        var isViewed: Boolean = false
) {
    class MainButton(
            @SerializedName("text")
            @Expose
            val text: String = "",
            @SerializedName("link")
            @Expose
            val link: String = ""
    )

    class Response(
            @SerializedName("umrahWidgetUmrahSayaByOrderUUID")
            @Expose
            val umrahWidgetSaya: MyUmrahEntity = MyUmrahEntity()
    )
}