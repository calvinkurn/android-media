package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CatalogBanner(
    @Expose @SerializedName("id")
    private var id: String = "",
    @Expose
    @SerializedName("title")
    var title: String = "",
    @Expose
    @SerializedName("imageUrl")
    var imageUrl: String = "",
    @Expose
    @SerializedName("redirectUrl")
    var redirectUrl: String = "",
    @Expose
    @SerializedName("promoCode")
    var promoCode: String = "",
)
