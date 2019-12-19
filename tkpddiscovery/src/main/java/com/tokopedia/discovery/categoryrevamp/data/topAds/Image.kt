package com.tokopedia.discovery.categoryrevamp.data.topAds

import com.google.gson.annotations.SerializedName

data class Image(

        @field:SerializedName("m_url")
        val mUrl: String? = null,

        @field:SerializedName("s_url")
        val sUrl: String = "",

        @field:SerializedName("xs_ecs")
        val xsEcs: String? = null,

        @field:SerializedName("s_ecs")
        val sEcs: String? = null,

        @field:SerializedName("xs_url")
        val xsUrl: String? = null,

        @field:SerializedName("m_ecs")
        val mEcs: String? = null
)