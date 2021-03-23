package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.discovery2.datamapper.discoveryPageData
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topads.sdk.domain.model.CpmModel
import java.util.*
import kotlin.collections.ArrayList

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

        @SerializedName("lihat_semua")
        val lihatSemua: LihatSemua? = null,

        @SerializedName("properties")
        var properties: Properties? = null,

        @SerializedName("additional_info")
        var compAdditionalInfo: ComponentAdditionalInfo? = null,

        @SerializedName("creative_name")
        var creativeName: String? = "",

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
        var parentComponentPosition: Int = 0,
        var cpmData: CpmModel? = null,
        var chipSelectionData: DataItem? = null,
        var chipSelectionChange: Boolean = false,
        var couponDetailClicked: Boolean = false,
        var couponAppliedClicked: Boolean = false,
        var position: Int = 0,
        var couponViewImpression: Boolean = false,
        var design: String = "v1",
        val filterController: FilterController = FilterController(),
        var searchParameter: SearchParameter = SearchParameter(),
        var filters: ArrayList<Filter> = ArrayList(),
        var selectedFilters: HashMap<String, String>? = null,
        var selectedSort: HashMap<String, String>? = null,
        var rpc_discoQuery:  Map<String, String?>? = null,
        var pinnedActiveTabId: String? = "",
        var dynamicOriginalId: String? = "",
        var showVerticalLoader: Boolean = false,
        var rpc_PinnedProduct: String? = "",
        var loadForHorizontal: Boolean = false,
        var pageLoadedCounter: Int = 1,
        var tabName: String? = "",
        var isSticky : Boolean = false,
        var description : String? = "",
        var showFilterCount: Boolean = true,
        var userAddressData: LocalCacheModel? = null,) {

    private var componentsItem: List<ComponentsItem>? = null

    fun setComponentsItem(listComponents: List<ComponentsItem>?, tabName: String? = "") {
        listComponents?.forEach {
            it.parentComponentId = this.id
            it.pageEndPoint = this.pageEndPoint
            it.tabName = tabName
            it.data?.firstOrNull()?.tabName = tabName
            discoveryPageData[this.pageEndPoint]?.componentMap?.set(it.id, it)
        }
        componentsItem = listComponents
    }

    fun getComponentsItem(): List<ComponentsItem>? {
        return componentsItem
    }

    fun reInitComponentItems(){
        componentsItem = null
    }
}



