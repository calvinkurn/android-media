package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey

data class DiscoveryResponse(

        @SerializedName("components",alternate = ["component_data"])
        var components: MutableList<ComponentsItem>,

        @SerializedName("component")
        var component: ComponentsItem? = null,

        @SerializedName("page_info")
        var pageInfo: PageInfo,

        @SerializedName("title")
        var title: String,

        @SerializedName("additional_info")
        val additionalInfo: AdditionalInfo?,

        @SerializedName("creative_name")
        val creativeName: String? = null

) {
        lateinit var componentMap: MutableMap<String, ComponentsItem>
        var cartMap: Map<MiniCartItemKey, MiniCartItem>? = null
        var sectionMap: MutableMap<String, Int>? = null
        var queryParamMap: MutableMap<String, String?>? = null
        var queryParamMapWithRpc: MutableMap<String, String> = mutableMapOf()
        var queryParamMapWithoutRpc: MutableMap<String, String> = mutableMapOf()
}
