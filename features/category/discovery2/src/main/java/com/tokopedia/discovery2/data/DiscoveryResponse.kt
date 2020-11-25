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
        val additionalInfo: AdditionalInfo?,

        @SerializedName("creative_name")
        val creativeName: String?

) {
    lateinit var componentMap: MutableMap<String, ComponentsItem>
}