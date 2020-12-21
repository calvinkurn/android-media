package com.tokopedia.logisticCommon.data.entity.trackingshipment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-10-17.
 */
data class Page (
        @SerializedName("additional_info")
        @Expose
        val listAdditionalInfo: List<AdditionalInfo> = listOf()
) {
    data class AdditionalInfo(
            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("notes")
            @Expose
            val notes: String = "",

            @SerializedName("url_detail")
            @Expose
            val urlDetail: String = "",

            @SerializedName("url_text")
            @Expose
            val urlText: String = ""
    )
}