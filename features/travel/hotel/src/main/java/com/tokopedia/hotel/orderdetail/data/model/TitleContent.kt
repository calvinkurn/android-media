package com.tokopedia.hotel.orderdetail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 13/05/19
 */

data class TitleContent(
        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("content")
        @Expose
        val content: String = ""
)
