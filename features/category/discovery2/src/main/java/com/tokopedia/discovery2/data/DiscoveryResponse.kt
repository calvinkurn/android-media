package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName

data class DiscoveryResponse(

        @SerializedName("components")
        val components: List<ComponentsItem?>? = null,

        @SerializedName("layout_info")
        val layoutInfo: LayoutInfo? = null,

        @SerializedName("seo_info")
        val seoInfo: SeoInfo? = null,

        @SerializedName("page_info")
        val pageInfo: PageInfo? = null,

        @SerializedName("title")
        val title: String? = ""
)