package com.tokopedia.feedplus.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/08/22
 */
data class TopAdslistImageShop(
    @SerializedName("cover_ecs")
    @Expose
    val coverEcs: String = "",

    @SerializedName("s_ecs")
    @Expose
    val sEcs: String = "",

    @SerializedName("xs_ecs")
    @Expose
    val xsEcs: String = "",

    @SerializedName("s_url")
    @Expose
    val sUrl: String = "",

    @SerializedName("xs_url")
    @Expose
    val xsUrl: String = ""
)
