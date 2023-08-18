package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.discovery2.data.claim_coupon.CatalogWithCouponList
import com.tokopedia.discovery2.data.mycoupon.MyCoupon
import com.tokopedia.discovery2.datamapper.discoveryPageData
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview.AutoPlayController
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

    @SerializedName("section_id")
    var sectionId: String = "",

    @SerializedName("title")
    val title: String? = "",

    @SerializedName("lihat_semua")
    var lihatSemua: LihatSemua? = null,

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
    var pageType: String = "",
    var parentComponentId: String = "",
    var parentComponentName: String? = null,
    var parentComponentPosition: Int = 0,
    var parentFilterComponentId: String? = null,
    var cpmData: CpmModel? = null,
    var chipSelectionData: DataItem? = null,
    var chipSelectionChange: Boolean = false,
    var couponDetailClicked: Boolean = false,
    var couponAppliedClicked: Boolean = false,
    var position: Int = 0,
    var couponViewImpression: Boolean = false,
    var design: String = "v1",
    var parentListSize: Int? = null,
    var filterController: FilterController = FilterController(),
    var searchParameter: SearchParameter = SearchParameter(),
    var filters: ArrayList<Filter> = ArrayList(),
    var selectedFilters: HashMap<String, String>? = null,
    var selectedSort: HashMap<String, String>? = null,
    var rpc_discoQuery: Map<String, String?>? = null,
    var pinnedActiveTabId: String? = "",
    var dynamicOriginalId: String? = "",
    var showVerticalLoader: Boolean = false,
    var rpc_PinnedProduct: String? = "",
    var recomQueryProdId: String? = null,
    var loadForHorizontal: Boolean = false,
    var pageLoadedCounter: Int = 1,
    var tabName: String? = "",
    var tabPosition: Int? = null,
    var parentSectionId: String? = "",
    var parentSectionCompID: String? = "",
    var isSticky: Boolean = false,
    var description: String? = "",
    var showFilterCount: Boolean = true,
    var shouldRefreshComponent: Boolean? = null,
    var autoPlayController: AutoPlayController? = null,
    var verticalProductFailState: Boolean = false,
    var errorState: ErrorState? = null,
    var userAddressData: LocalCacheModel? = null,
    var nextPageKey: String? = "",
    var isFromCategory: Boolean = false,
    var isLottieAlreadyAnimated: Boolean = false,
    var horizontalProductFailState: Boolean = false,
    var isSelectedFiltersFromQueryApplied: Boolean = false,
    var isExplicitWidgetHidden: Boolean = false,
    var myCouponList: List<MyCoupon>? = null,
    var claimCouponList: List<CatalogWithCouponList>? = null
) {

    private var componentsItem: List<ComponentsItem>? = null

    fun setComponentsItem(listComponents: List<ComponentsItem>?, tabName: String? = "") {
        listComponents?.forEach {
            it.parentComponentId = this.id
            it.pageEndPoint = this.pageEndPoint
            it.tabName = tabName
            it.data?.firstOrNull()?.tabName = tabName
            it.parentComponentPosition = position
            discoveryPageData[this.pageEndPoint]?.componentMap?.set(it.id, it)
        }
        componentsItem = listComponents
    }

    fun getComponentsItem(): List<ComponentsItem>? {
        return componentsItem
    }

    fun reInitComponentItems() {
        componentsItem = null
    }
}
