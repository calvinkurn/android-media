package com.tokopedia.feedplus.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/08/22
 */
data class TopAdslistImage(
    @SerializedName("m_url")
    @Expose
    val mUrl: String = "",

    @SerializedName("s_url")
    @Expose
    val sUrl: String = "",

    @SerializedName("xs_url")
    @Expose
    val xsUrl: String = "",

    @SerializedName("m_ecs")
    @Expose
    val mEcs: String = "",

    @SerializedName("s_ecs")
    @Expose
    val sEcs: String = "",

    @SerializedName("xs_ecs")
    @Expose
    val xsEcs: String = ""
)
