package com.tokopedia.hotel.orderdetail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 13/05/19
 */

data class TitleText(
        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("text")
        @Expose
        val text: String = ""
)
