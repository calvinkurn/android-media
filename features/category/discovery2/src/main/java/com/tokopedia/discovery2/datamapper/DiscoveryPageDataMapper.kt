package com.tokopedia.discovery2.datamapper

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.Constant.Calendar.DYNAMIC
import com.tokopedia.discovery2.Constant.Calendar.STATIC
import com.tokopedia.discovery2.Constant.ProductTemplate.GRID
import com.tokopedia.discovery2.Constant.TopAdsSdk.TOP_ADS_GSLP_TDN
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.Utils.Companion.TIMER_DATE_FORMAT
import com.tokopedia.discovery2.Utils.Companion.areFiltersApplied
import com.tokopedia.discovery2.Utils.Companion.getElapsedTime
import com.tokopedia.discovery2.Utils.Companion.isSaleOver
import com.tokopedia.discovery2.Utils.Companion.parseFlashSaleDate
import com.tokopedia.discovery2.analytics.EMPTY_STRING
import com.tokopedia.discovery2.data.*
import com.tokopedia.discovery2.data.ErrorState.NetworkErrorState
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.ACTIVE_TAB
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.CATEGORY_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.RECOM_PRODUCT_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.TARGET_COMP_ID
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview.AutoPlayController
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartItemType
import com.tokopedia.minicart.common.domain.data.getMiniCartItemParentProduct
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import java.util.LinkedList


val discoveryPageData: MutableMap<String, DiscoveryResponse> = HashMap()
const val DYNAMIC_COMPONENT_IDENTIFIER = "dynamic_"
const val SHIMMER_ITEMS_LIST_SIZE = 10
const val COMPONENTS_PER_PAGE = 10
var discoComponentQuery: MutableMap<String, String?>? = null

fun mapDiscoveryResponseToPageData(discoveryResponse: DiscoveryResponse,
                                   queryParameterMap: MutableMap<String, String?>,
                                   userAddressData: LocalCacheModel?,isLoggedIn:Boolean,shouldHideSingleProdCard:Boolean): DiscoveryPageData {
    val pageInfo = discoveryResponse.pageInfo
    val discoveryPageData = DiscoveryPageData(pageInfo, discoveryResponse.additionalInfo)
    discoComponentQuery = queryParameterMap
    val discoveryDataMapper = DiscoveryPageDataMapper(pageInfo, queryParameterMap, discoveryResponse.queryParamMapWithRpc, discoveryResponse.queryParamMapWithoutRpc, userAddressData, isLoggedIn, shouldHideSingleProdCard)
    if (!discoveryResponse.components.isNullOrEmpty()) {
        discoveryPageData.components = discoveryDataMapper.getDiscoveryComponentListWithQueryParam(discoveryResponse.components.filter {
            pageInfo.identifier?.let { identifier ->
                it.pageEndPoint = identifier
            }
            pageInfo.path?.let { path ->
                it.pagePath = path
            }
            discoveryResponse.componentMap[it.id] = it
            it.renderByDefault
        })
    }
    discoveryResponse.component?.setComponentsItem(discoveryResponse.components)
    return discoveryPageData
}

class DiscoveryPageDataMapper(
    private val pageInfo: PageInfo,
    private val queryParameterMap: Map<String, String?>,
    private val queryParameterMapWithRpc: Map<String, String>,
    private val queryParameterMapWithoutRpc: Map<String, String>,
    private val localCacheModel: LocalCacheModel?,
    private val isLoggedIn: Boolean,
    private val shouldHideSingleProdCard: Boolean
) {
    fun getDiscoveryComponentListWithQueryParam(components: List<ComponentsItem>): List<ComponentsItem> {
        val targetCompId = queryParameterMap[TARGET_COMP_ID] ?: ""
        val componentList = getDiscoComponentListFromResponse(filterSaleTimer(components))
        if (componentList.isNotEmpty() && targetCompId.isNotEmpty()) {
            componentList.forEach { item ->
                if (item.id == targetCompId) {
                    item.rpc_discoQuery = queryParameterMap
                }
                item.userAddressData = localCacheModel
            }
        } else if (componentList.isNotEmpty()) {
            componentList.forEach { item ->
                item.userAddressData = localCacheModel
            }
        }
        return componentList
    }

    private fun filterSaleTimer(componentList: List<ComponentsItem>): ArrayList<ComponentsItem> {
        val listComponents: ArrayList<ComponentsItem> = ArrayList()
        var itemIdTobeRemoved: String? = "0"
        componentList.forEach {
            if (it.name == ComponentNames.FlashSaleTimer.componentName || (itemIdTobeRemoved != "0" && it.id == itemIdTobeRemoved)) {
                when {
                    isSaleOver(parseFlashSaleDate(it.data?.firstOrNull()?.ongoingCampaignEndTime), TIMER_DATE_FORMAT) -> {
                        itemIdTobeRemoved = it.data?.firstOrNull()?.flashTimerTargetComponent ?: "0"
                    }
                    it.id == itemIdTobeRemoved -> {
                        itemIdTobeRemoved = "0"
                    }
                    else -> {
                        listComponents.add(it)
                    }
                }
            } else {
                listComponents.add(it)
            }
        }
        return listComponents
    }

    private fun getDiscoveryComponentList(components: List<ComponentsItem>): List<ComponentsItem> {
        val listComponents: ArrayList<ComponentsItem> = ArrayList()
        for ((position, component) in components.withIndex()) {
            listComponents.addAll(parseComponent(component, position))
        }
        return listComponents
    }

    private fun getDiscoComponentListFromResponse(components: List<ComponentsItem>): List<ComponentsItem> {
        val listComponents: ArrayList<ComponentsItem> = ArrayList()
        for (component in components) {
            listComponents.addAll(parseComponent(component, listComponents.size))
        }
        return listComponents
    }

    private fun parseComponent(component: ComponentsItem, position: Int): List<ComponentsItem> {
        val listComponents: ArrayList<ComponentsItem> = ArrayList()
        component.position = position
        when (component.name) {
            ComponentNames.Tabs.componentName -> listComponents.addAll(parseTab(component, position))
            ComponentNames.ProductCardRevamp.componentName,
            ComponentNames.ProductCardSprintSale.componentName -> {
                addRecomQueryProdID(component)
                listComponents.addAll(parseProductVerticalList(component))
            }
            ComponentNames.BannerInfinite.componentName -> listComponents.addAll(parseProductVerticalList(component,false))
            ComponentNames.ContentCard.componentName -> listComponents.addAll(parseProductVerticalList(component,false))
            ComponentNames.ShopCardInfinite.componentName -> listComponents.addAll(parseProductVerticalList(component,false))
            ComponentNames.ProductCardSprintSaleCarousel.componentName,
            ComponentNames.ProductCardCarousel.componentName -> {
                addRecomQueryProdID(component)
                updateCarouselWithCart(component)
                listComponents.add(component)
            }
            ComponentNames.Section.componentName ->{
                saveSectionPosition(component.pageEndPoint, component.sectionId, component.position)
                listComponents.addAll(parseSectionComponent(component))
            }
            ComponentNames.QuickCoupon.componentName -> {
                if (component.isApplicable) {
                    listComponents.add(component)
                }
            }

            ComponentNames.QuickFilter.componentName -> {
                listComponents.add(component)
                component.properties?.targetId?.let {
                    getComponent(it,component.pageEndPoint).apply {
                        this?.parentFilterComponentId = component.id
                    }
                }
            }
            ComponentNames.CalendarWidgetGrid.componentName,
            ComponentNames.CalendarWidgetCarousel.componentName -> {
                listComponents.add(component)
                if(component.properties?.calendarType.equals(DYNAMIC)
                    && component.properties?.calendarLayout.equals(GRID))
                    listComponents.addAll(parseProductVerticalList(component, false))
                else if(component.properties?.calendarType == STATIC){
                    if(component.getComponentsItem().isNullOrEmpty()) {
                        component.setComponentsItem(
                            DiscoveryDataMapper().mapListToComponentList(
                                component.data ?: arrayListOf(),
                                ComponentNames.CalendarWidgetItem.componentName,
                                component.properties,
                                component.creativeName,
                                parentComponentPosition = component.position,
                                parentSectionId = component.parentSectionId
                            )
                        )
                    }
                    if(component.properties?.calendarLayout.equals(GRID)) {
                        component.getComponentsItem()?.let {
                            listComponents.addAll(getDiscoveryComponentList(it))
                        }
                    }
                }
            }

            ComponentNames.SingleBanner.componentName, ComponentNames.DoubleBanner.componentName,
            ComponentNames.TripleBanner.componentName, ComponentNames.QuadrupleBanner.componentName ->
                listComponents.add(DiscoveryDataMapper.mapBannerComponentData(component))
            ComponentNames.BannerTimer.componentName -> {
                if (addBannerTimerComp(component)) {
                    listComponents.add(component)
                }
            }
            ComponentNames.Video.componentName -> {
                addAutoPlayController(component)
                listComponents.add(component)
            }
            ComponentNames.MerchantVoucherList.componentName -> {
                if(isLoggedIn)
                listComponents.addAll(setupMerchantVoucherList(component))
            }
            ComponentNames.ProductCardSingle.componentName -> {
                if (!shouldHideSingleProdCard) {
                    addRecomQueryProdID(component)
                    listComponents.add(component)
                }
            }
            ComponentNames.ExplicitWidget.componentName -> {
                addPageInfoToExplicitWidget(component)
                listComponents.add(component)
            }
            else -> listComponents.add(component)
        }
        return listComponents
    }

    private fun addRecomQueryProdID(component: ComponentsItem) {
        if (!queryParameterMap[RECOM_PRODUCT_ID].isNullOrEmpty())
            component.recomQueryProdId = queryParameterMap[RECOM_PRODUCT_ID]
    }

    private fun saveSectionPosition(pageEndPoint: String, sectionId: String, position: Int) {
        if (getSectionPositionMap(pageEndPoint) == null) {
            setSectionPositionMap(mutableMapOf(), pageEndPoint)
        }
        getSectionPositionMap(pageEndPoint)?.let {
            it.put(sectionId, position)
        }
    }

    private fun addAutoPlayController(component: ComponentsItem) {
        if (component.autoPlayController == null) {
            val autoplayComponent: ComponentsItem = getAutoPlayComponent(component)
            autoplayComponent.autoPlayController?.videoIdSet?.add(component.id)
            component.autoPlayController = autoplayComponent.autoPlayController
        } else if (getComponent(AutoPlayController.AUTOPLAY_ID, component.pageEndPoint) == null) {
            setComponent(
                AutoPlayController.AUTOPLAY_ID,
                component.pageEndPoint,
                ComponentsItem().apply { autoPlayController = component.autoPlayController })
        }
    }

    private fun getAutoPlayComponent(component: ComponentsItem): ComponentsItem {
        return getComponent(AutoPlayController.AUTOPLAY_ID, component.pageEndPoint) ?: run {
            ComponentsItem().apply {
                autoPlayController = AutoPlayController(component.id)
                setComponent(AutoPlayController.AUTOPLAY_ID, component.pageEndPoint, this)
            }
        }
    }

    private fun setupMerchantVoucherList(component: ComponentsItem): List<ComponentsItem> {
        component.properties?: kotlin.run {
            component.properties  = Properties()
        }
        component.properties?.template = Constant.ProductTemplate.LIST
        component.componentsPerPage = COMPONENTS_PER_PAGE
        return parseProductVerticalList(component, component.areFiltersApplied())
    }

    private fun addBannerTimerComp(component: ComponentsItem): Boolean {
        if(component.data?.firstOrNull()?.endDate.isNullOrEmpty() || component.data?.firstOrNull()?.startDate.isNullOrEmpty()){
            return false
        }
        return getElapsedTime(component.data?.firstOrNull()?.endDate ?: "") > 0
    }

    private fun addPageInfoToExplicitWidget(component: ComponentsItem){
        component.pageType = pageInfo.type ?: EMPTY_STRING
    }

    private fun parseTab(component: ComponentsItem, position: Int): List<ComponentsItem> {
        val listComponents: ArrayList<ComponentsItem> = ArrayList()
        val isDynamicTabs = component.properties?.dynamic ?: false
        component.pinnedActiveTabId = queryParameterMap[ACTIVE_TAB]
        component.parentComponentPosition = position
        when {
            isDynamicTabs -> {
                listComponents.add(component)
            }
            !component.data.isNullOrEmpty() -> {
                listComponents.add(component)
            }
            else -> {
                return listComponents
            }
        }
        if (component.getComponentsItem().isNullOrEmpty()) {
            component.setComponentsItem(DiscoveryDataMapper.mapTabsListToComponentList(component, ComponentNames.TabsItem.componentName), component.tabName)
        }
        component.getComponentsItem()?.forEachIndexed { index, it ->
            it.apply {
                if (!data.isNullOrEmpty()) {
                    data?.get(0)?.let { tabData ->
                        if (tabData.isSelected) {
                            val saleTabStatus = checkSaleTimer(this)
                            val targetComponentIdList = tabData.targetComponentId?.split(",")?.map {
                                if (isDynamicTabs) DYNAMIC_COMPONENT_IDENTIFIER + index + it.trim() else it.trim()
                            }
                            if (!targetComponentIdList.isNullOrEmpty()) {
                                val tabsChildComponentsItemList: ArrayList<ComponentsItem> = ArrayList()
                                targetComponentIdList.forEachIndexed { compIndex,componentId ->
                                    if (isDynamicTabs) {
                                        handleDynamicTabsComponents(componentId, index, component, tabData.name)?.let {
                                            tabsChildComponentsItemList.add(it)
                                            listComponents.addAll(parseComponent(it,
                                                position + compIndex + 1))
                                        }
                                    } else {
                                        handleAvailableComponents(componentId, component, tabData.name)?.let {
                                            tabsChildComponentsItemList.add(it)
                                            listComponents.addAll(parseComponent(it,
                                                position + compIndex + 1))
                                        }
                                    }
                                }
                                if (saleTabStatus) {
                                    val componentList = handleProductState(this, ComponentNames.SaleEndState.componentName)
                                    tabsChildComponentsItemList.addAll(componentList)
                                    listComponents.addAll(componentList)
                                }
                                this.setComponentsItem(tabsChildComponentsItemList, tabData.name)
                            }
                        }
                    }
                }
            }
        }
        return listComponents
    }

    private fun handleDynamicTabsComponents(targetedComponentId: String, tabItemIndex: Int, tabComponent: ComponentsItem, tabName: String?): ComponentsItem? {
        var tabChildComponentsItem: ComponentsItem? = null
        val pageIdentity = pageInfo.identifier ?: ""
        val originalComponentId = targetedComponentId.removePrefix("$DYNAMIC_COMPONENT_IDENTIFIER$tabItemIndex")
        if (getComponent(targetedComponentId, pageIdentity) == null) {
            getComponent(originalComponentId, pageIdentity)?.let { component1 ->
                component1.copy().apply {
                    parentComponentId = tabComponent.id
                    id = targetedComponentId
                    this.tabName = tabName
                    dynamicOriginalId = originalComponentId
                    filterController = FilterController()
                    properties = component1.properties
                    properties?.dynamic = tabComponent.properties?.dynamic ?: false
                    setComponent(targetedComponentId, pageIdentity, this)
                    tabChildComponentsItem = this
                }
            }
        } else {
            tabChildComponentsItem = handleAvailableComponents(targetedComponentId, tabComponent, tabName)
        }
        return tabChildComponentsItem
    }

    private fun handleAvailableComponents(targetedComponentId: String, tabComponent: ComponentsItem, tabName: String?): ComponentsItem? {
        val pageIdentity = pageInfo.identifier ?: ""
        var tabChildComponentsItem: ComponentsItem? = null
        getComponent(targetedComponentId, pageIdentity)?.let { component1 ->
            component1.parentComponentId = tabComponent.id
            component1.tabName = tabName
            tabChildComponentsItem = component1
        }
        return tabChildComponentsItem
    }

    private fun checkSaleTimer(tab: ComponentsItem): Boolean {
        tab.apply {
            if (!data.isNullOrEmpty()) {
                data?.get(0)?.let { tabData ->
                    val targetComponentIdList = tabData.targetComponentId?.split(",")?.map { it.trim() }
                    if (!targetComponentIdList.isNullOrEmpty()) {
                        targetComponentIdList.forEach { componentId ->
                            getComponent(componentId, pageInfo.identifier!!)?.let { componentItem ->
                                if (componentItem.name == ComponentNames.TimerSprintSale.componentName) {
                                    if (!componentItem.data.isNullOrEmpty() && Utils.isSaleOver(componentItem.data!![0].endDate
                                                    ?: "")) {
                                        data!![0].targetComponentId = componentId
                                        return true
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false
    }

    private fun parseProductVerticalList(component: ComponentsItem,showEmptyState:Boolean = true): List<ComponentsItem> {
        val listComponents: LinkedList<ComponentsItem> = LinkedList()

        if (component.verticalProductFailState) {
            listComponents.add(component)
            component.getComponentsItem()?.let {
                listComponents.addAll(getDiscoveryComponentList(it))
            }
            when(component.errorState){
                NetworkErrorState ->{
                    listComponents.addAll(handleProductState(component, ComponentNames.ProductListNetworkErrorLoad.componentName, queryParameterMap))
                }
                else -> {
                    listComponents.addAll(handleProductState(component, ComponentNames.ProductListErrorLoad.componentName, queryParameterMap))
                }
            }
        } else {
            if (component.getComponentsItem().isNullOrEmpty() && component.noOfPagesLoaded == 0 && !component.verticalProductFailState) {
                listComponents.add(component.copy().apply {
                    setComponentsItem(component.getComponentsItem(), component.tabName)
                })
                component.needPagination = true
                component.userAddressData = localCacheModel
                listComponents.addAll(List(SHIMMER_ITEMS_LIST_SIZE) {
                    ComponentsItem(name =
                    if(component.name == ComponentNames.CalendarWidgetGrid.componentName)
                        ComponentNames.ShimmerCalendarWidget.componentName
                    else
                        ComponentNames.ShimmerProductCard.componentName).apply {
                        properties = component.properties
                        parentComponentName = component.name
                    }
                })
            } else {
                listComponents.add(component)
                component.getComponentsItem()?.let {
                    listComponents.addAll(getDiscoveryComponentList(it).apply {
                        if(component.properties?.tokonowATCActive == true) {
                            updateWithCart(it, getCartData(component.pageEndPoint))
                        }
                    })
                }
                if (component.properties?.index != null &&
                    component.properties?.index!! > Int.ZERO &&
                    component.properties?.index!! < listComponents.size &&
                    !component.properties?.targetedComponentId.isNullOrEmpty())
                 {
                    getComponent(
                        component.properties?.targetedComponentId!!,
                        component.pageEndPoint
                    )?.let {
                        if(it.name == ComponentNames.DiscoTDNBanner.componentName){
                            it.design = TOP_ADS_GSLP_TDN
                            it.recomQueryProdId = component.recomQueryProdId
                        }
                        listComponents.add(component.properties?.index!! + Int.ONE, it)
                    }
                }

                if (Utils.nextPageAvailable(component,component.componentsPerPage) && component.showVerticalLoader) {
                    listComponents.addAll(handleProductState(component, ComponentNames.LoadMore.componentName, queryParameterMap))
                } else if (component.getComponentsItem()?.size == 0 && showEmptyState) {
                    listComponents.addAll(handleProductState(component, ComponentNames.ProductListEmptyState.componentName, queryParameterMap))
                }
            }
        }
        return listComponents
    }

    private fun handleProductState(component: ComponentsItem, componentName: String, queryParameterMap: Map<String, String?>? = null): ArrayList<ComponentsItem> {
        val productState: ArrayList<ComponentsItem> = ArrayList()
        productState.add(ComponentsItem(name = componentName).apply {
            pageEndPoint = component.pageEndPoint
            parentComponentId = component.id
            parentComponentName = component.name
            rpc_discoQuery = queryParameterMap
            id = componentName
            discoveryPageData[this.pageEndPoint]?.componentMap?.set(this.id, this)
        })
        return productState
    }

    private fun updateCarouselWithCart(component: ComponentsItem){
        if(component.properties?.tokonowATCActive == true) {
            component.getComponentsItem()?.let {
                if(updateWithCart(it,getCartData(component.pageEndPoint))){
                    component.shouldRefreshComponent = true
                }
            }
        }
    }

    private fun updateWithCart(list: List<ComponentsItem>, map: Map<MiniCartItemKey, MiniCartItem>?) : Boolean {
        var shouldRefresh = false
        if (map == null) return shouldRefresh
        list.forEach { item ->
            item.data?.firstOrNull()?.let { dataItem ->
                if (dataItem.hasATC && !dataItem.parentProductId.isNullOrEmpty() && map.containsKey(MiniCartItemKey(dataItem.parentProductId ?: "", type = MiniCartItemType.PARENT))) {
                    map.getMiniCartItemParentProduct(dataItem.parentProductId ?: "")?.totalQuantity?.let { quantity ->
                        if(updateQuantity(quantity, item))
                            shouldRefresh = true
                    }
                } else if (dataItem.hasATC && !dataItem.productId.isNullOrEmpty() && map.containsKey(MiniCartItemKey(dataItem.productId ?: ""))) {
                    map.getMiniCartItemProduct(dataItem.productId ?: "")?.quantity?.let { quantity ->
                        if(updateQuantity(quantity, item))
                            shouldRefresh = true
                    }
                } else {
                    if(updateQuantity(0, item))
                        shouldRefresh = true
                }
            }
        }
        return shouldRefresh
    }

    private fun updateQuantity(quantity:Int,item:ComponentsItem):Boolean{
        if (quantity != item.data?.firstOrNull()?.quantity) {
            item.data?.firstOrNull()?.quantity = quantity
            item.shouldRefreshComponent = true
            return true
        }
        return false
    }

    private fun parseSectionComponent(component: ComponentsItem): List<ComponentsItem> {
        val listComponents: ArrayList<ComponentsItem> = ArrayList()
        listComponents.add(component)
        component.getComponentsItem()?.let {
            listComponents.addAll(getSectionComponentList(it, component.position + 1))
        }
        return listComponents
    }

    private fun getSectionComponentList(
        components: List<ComponentsItem>,
        sectionPosition: Int
    ): List<ComponentsItem> {
        val listComponents: ArrayList<ComponentsItem> = ArrayList()
        for ((position, component) in components.withIndex()) {
            listComponents.addAll(
                parseComponent(
                    component,
                    sectionPosition + position
                )
            )
        }
        return listComponents
    }
}

fun getComponent(componentId: String, pageName: String): ComponentsItem? {
    discoveryPageData[pageName]?.let {
        return it.componentMap[componentId]
    }
    return null
}

fun setComponent(componentId: String, pageName: String, componentsItem: ComponentsItem) {
    discoveryPageData[pageName]?.let {
        it.componentMap[componentId] = componentsItem
    }
}

fun getCartData(pageName: String):Map<MiniCartItemKey, MiniCartItem>?{
    discoveryPageData[pageName]?.let {
        return it.cartMap
    }
    return null
}

fun setCartData(cartMap:Map<MiniCartItemKey, MiniCartItem>, pageName: String){
    discoveryPageData[pageName]?.let {
        it.cartMap = cartMap
    }
}

fun setSectionPositionMap(map: MutableMap<String, Int>, pageName: String) {
    discoveryPageData[pageName]?.let {
        it.sectionMap = map
    }
}

fun getSectionPositionMap(pageName: String):MutableMap<String, Int>?{
    discoveryPageData[pageName]?.let {
        return it.sectionMap
    }
    return null
}

fun updateComponentsQueryParams(categoryId: String) {
    discoComponentQuery?.let {
        it[CATEGORY_ID] = categoryId
    }
}

fun getPageInfo(pageName: String): PageInfo {
    discoveryPageData[pageName]?.let {
        return it.pageInfo
    }
    return PageInfo()
}

fun getMapWithoutRpc(pageName: String): Map<String, String>? {
    discoveryPageData[pageName]?.let {
        return it.queryParamMapWithoutRpc
    }
    return null
}

fun getMapWithRpc(pageName: String): Map<String, String>? {
    discoveryPageData[pageName]?.let {
        return it.queryParamMapWithRpc
    }
    return null
}
