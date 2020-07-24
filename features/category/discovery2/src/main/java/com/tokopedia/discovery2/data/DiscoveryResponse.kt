package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName

data class DiscoveryResponse(

        @SerializedName("components")
        var components: MutableList<ComponentsItem>,

        @SerializedName("component")
        val component: ComponentsItem? = null,

        @SerializedName("page_info")
        var pageInfo: PageInfo,

        @SerializedName("title")
        val title: String,

        @SerializedName("additional_info")
        val additionalInfo: AdditionalInfo?

) {
    lateinit var componentMap: MutableMap<String, ComponentsItem>
}