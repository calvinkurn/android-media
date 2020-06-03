package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName

data class DiscoveryResponse(

        @SerializedName("components")
        var components: MutableList<ComponentsItem>? = null,

        @SerializedName("component")
        val component: ComponentsItem? = null,


        @SerializedName("page_info")
        val pageInfo: PageInfo? = null,

        @SerializedName("title")
        val title: String? = ""

)