package com.tokopedia.discovery2.datamapper

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.Utils.Companion.TIMER_DATE_FORMAT
import com.tokopedia.discovery2.Utils.Companion.getElapsedTime
import com.tokopedia.discovery2.Utils.Companion.isSaleOver
import com.tokopedia.discovery2.Utils.Companion.parseFlashSaleDate
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.data.PageInfo
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.ACTIVE_TAB
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.CATEGORY_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.TARGET_COMP_ID
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel


val discoveryPageData: MutableMap<String, DiscoveryResponse> = HashMap()
const val DYNAMIC_COMPONENT_IDENTIFIER = "dynamic_"
var discoComponentQuery: MutableMap<String, String?>? = null

fun mapDiscoveryResponseToPageData(discoveryResponse: DiscoveryResponse,
                                   queryParameterMap: MutableMap<String, String?>,
                                   userAddressData: LocalCacheModel?): DiscoveryPageData {
    val pageInfo = discoveryResponse.pageInfo
    val discoveryPageData = DiscoveryPageData(pageInfo, discoveryResponse.additionalInfo)
    discoComponentQuery = queryParameterMap
    val discoveryDataMapper = DiscoveryPageDataMapper(pageInfo, queryParameterMap, userAddressData)
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

class DiscoveryPageDataMapper(private val pageInfo: PageInfo,
                              private val queryParameterMap: Map<String, String?>,
                              private val localCacheModel: LocalCacheModel?) {
    fun getDiscoveryComponentListWithQueryParam(components: List<ComponentsItem>): List<ComponentsItem> {
        val targetCompId = queryParameterMap[TARGET_COMP_ID] ?: ""
        val componentList = getDiscoveryComponentList(filterSaleTimer(components))
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

    private fun parseComponent(component: ComponentsItem, position: Int): List<ComponentsItem> {
        val listComponents: ArrayList<ComponentsItem> = ArrayList()
        component.position = position
        when (component.name) {
            ComponentNames.Tabs.componentName -> listComponents.addAll(parseTab(component, position))
            ComponentNames.ProductCardRevamp.componentName,
            ComponentNames.ProductCardSprintSale.componentName -> listComponents.addAll(parseProductVerticalList(component))
            ComponentNames.QuickCoupon.componentName -> {
                if (component.isApplicable) {
                    listComponents.add(component)
                }
            }

//            ComponentNames.ProductCardCarousel.componentName,
//            ComponentNames.ProductCardSprintSaleCarousel.componentName -> {
//                if(component.productCarouselFailState){
// Comment                    component.productCarouselFailState = false
//                    listComponents.add(component.copy().apply {
//                        setComponentsItem(component.getComponentsItem())
//                        productCarouselFailState = false
//                    })
//                }else{
//                    listComponents.add(component)
//                }
//            }

            ComponentNames.QuickFilter.componentName -> {
                listComponents.add(component.copy())
            }
            ComponentNames.SingleBanner.componentName, ComponentNames.DoubleBanner.componentName,
            ComponentNames.TripleBanner.name, ComponentNames.QuadrupleBanner.componentName ->
                listComponents.add(DiscoveryDataMapper.mapBannerComponentData(component))
            ComponentNames.BannerTimer.componentName -> {
                if (addBannerTimerComp(component)) {
                    listComponents.add(component)
                }
            }
            else -> listComponents.add(component)
        }
        return listComponents
    }

    private fun addBannerTimerComp(component: ComponentsItem): Boolean {
        return getElapsedTime(component.data?.firstOrNull()?.endDate ?: "") > 0
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
                                targetComponentIdList.forEach { componentId ->
                                    if (isDynamicTabs) {
                                        handleDynamicTabsComponents(componentId, index, component, tabData.name)?.let {
                                            tabsChildComponentsItemList.add(it)
                                            listComponents.addAll(parseComponent(it, position))
                                        }
                                    } else {
                                        handleAvailableComponents(componentId, component, tabData.name)?.let {
                                            tabsChildComponentsItemList.add(it)
                                            listComponents.addAll(parseComponent(it, position))
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

    //    private fun parseProductVerticalList(component: ComponentsItem): List<ComponentsItem> {
//        val listComponents: ArrayList<ComponentsItem> = ArrayList()
//        if (component.getComponentsItem().isNullOrEmpty() && component.noOfPagesLoaded == 0) {
//            listComponents.add(component.copy().apply {
//                setComponentsItem(component.getComponentsItem(), component.tabName)
//            })
//            component.needPagination = true
//            component.userAddressData = localCacheModel
//            listComponents.addAll(List(10) { ComponentsItem(name = ComponentNames.ShimmerProductCard.componentName).apply {
//                properties = component.properties
//            } })
//        } else {
//            listComponents.add(component)
//            component.getComponentsItem()?.let {
//                listComponents.addAll(getDiscoveryComponentList(it))
//            }
//            if (component.getComponentsItem()?.size.isMoreThanZero() &&
//                    component.getComponentsItem()?.size?.rem(component.componentsPerPage) == 0
//                    && component.showVerticalLoader || component.verticalProductFailState) {
//                listComponents.addAll(handleProductState(component, ComponentNames.LoadMore.componentName, queryParameterMap))
//            } else if (component.getComponentsItem()?.size == 0) {
//                listComponents.addAll(handleProductState(component, ComponentNames.ProductListEmptyState.componentName, queryParameterMap))
//            }
//        }
//        return listComponents
//    }

    private fun parseProductVerticalList(component: ComponentsItem): List<ComponentsItem> {
        val listComponents: ArrayList<ComponentsItem> = ArrayList()

        if (component.verticalProductFailState) {
            listComponents.add(component)
            component.getComponentsItem()?.let {
                listComponents.addAll(getDiscoveryComponentList(it))
            }

            listComponents.addAll(handleProductState(component, ComponentNames.ProductListErrorLoad.componentName, queryParameterMap))
        } else {
            if (component.getComponentsItem().isNullOrEmpty() && component.noOfPagesLoaded == 0 && !component.verticalProductFailState) {
                listComponents.add(component.copy().apply {
                    setComponentsItem(component.getComponentsItem(), component.tabName)
                })
                component.needPagination = true
                component.userAddressData = localCacheModel
                listComponents.addAll(List(10) {
                    ComponentsItem(name = ComponentNames.ShimmerProductCard.componentName).apply {
                        properties = component.properties
                    }
                })
            } else {
                listComponents.add(component)
                component.getComponentsItem()?.let {
                    listComponents.addAll(getDiscoveryComponentList(it))
                }
                if (component.getComponentsItem()?.size.isMoreThanZero() && component.getComponentsItem()?.size?.rem(component.componentsPerPage) == 0 && component.showVerticalLoader) {
                    listComponents.addAll(handleProductState(component, ComponentNames.LoadMore.componentName, queryParameterMap))
                } else if (component.getComponentsItem()?.size == 0) {
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
            rpc_discoQuery = queryParameterMap
            id = componentName
            discoveryPageData[this.pageEndPoint]?.componentMap?.set(this.id, this)
        })
        return productState
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