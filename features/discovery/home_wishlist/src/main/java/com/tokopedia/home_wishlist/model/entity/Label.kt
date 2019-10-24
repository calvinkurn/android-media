package com.tokopedia.home_wishlist.model.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Label(
    @SerializedName("title")
    @Expose
    var title: String? = null,
    @SerializedName("color")
    @Expose
    var color: String? = null

)