package com.tokopedia.homenav.mainnav.data.pojo.favoriteshop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopPaging(
    @SerializedName("current")
    @Expose
    val current: Int? = 0,
    @SerializedName("uri_previous")
    @Expose
    val uriPrevious: Int? = 0,
    @SerializedName("uri_next")
    @Expose
    val uriNext: Int? = 0
)
