package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.discovery2.datamapper.discoveryPageData
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.topads.sdk.domain.model.CpmModel
import java.util.HashMap

data class ComponentsItem(

        @SerializedName("render_by_default")
        val renderByDefault: Boolean = false,

        @SerializedName("data")
        var data: List<DataItem>? = null,

        @SerializedName("ab_modulo")
        val abModulo: String? = "",

        @SerializedName("name")
        var name: String? = "",

        @SerializedName("id")
        var id: String = "",

        @SerializedName("title")
        val title: String? = "",

        @SerializedName("properties")
        var properties: Properties? = null,

        var isApplicable: Boolean = true,

        var topAdsTrackingStatus: Boolean = false,

        var shimmerHeight: Int = 0,

        var shimmerWidth: Int = 0,

        var constraintRatio: String? = null,

        var needPagination: Boolean = false,
        var noOfPagesLoaded: Int = 0,
        var componentsPerPage: Int = 20,
        var pageEndPoint: String = "",
        var pagePath: String = "",
        var parentComponentId: String = "",
        var cpmData: CpmModel? = null,
        var chipSelectionData: DataItem? = null,
        var selectedFilters : HashMap<String, String>? = null,
        var selectedSort : HashMap<String, String>? = null,
        var chipSelectionChange: Boolean = false,
        var couponDetailClicked: Boolean = false,
        var couponAppliedClicked: Boolean = false,
        var position: Int = 0,
        var couponViewImpression: Boolean = false,
        var design: String = "",
        val filterController: FilterController = FilterController(),
        var searchParameter: SearchParameter = SearchParameter(),
        var filters: ArrayList<Filter> = ArrayList(),
        var rpc_PinnedProduct: String? = "") {

    private var componentsItem: List<ComponentsItem>? = null

    fun setComponentsItem(listComponents: List<ComponentsItem>?) {
        listComponents?.forEach {
            it.parentComponentId = this.id
            it.pageEndPoint = this.pageEndPoint
            discoveryPageData[this.pageEndPoint]?.componentMap?.set(it.id, it)
        }
        componentsItem = listComponents
    }

    fun getComponentsItem(): List<ComponentsItem>? {
        return componentsItem
    }

}



