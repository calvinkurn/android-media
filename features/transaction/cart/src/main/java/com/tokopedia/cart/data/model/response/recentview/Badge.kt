package com.tokopedia.cart.data.model.response.recentview

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-06-18.
 */

data class Badge(
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("image_url")
        @Expose
        val imageUrl: String = ""
)