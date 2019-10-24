package com.tokopedia.home_wishlist.model.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Badge {
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("img_url")
    @Expose
    var imgUrl: String? = null
    @SerializedName("image_url")
    @Expose
    var imageUrl: String? = null
}