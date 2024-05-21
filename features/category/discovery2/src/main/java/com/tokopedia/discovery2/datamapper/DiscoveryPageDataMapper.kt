package com.tokopedia.discovery2.datamapper

import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.Constant.Calendar.DYNAMIC
import com.tokopedia.discovery2.Constant.Calendar.STATIC
import com.tokopedia.discovery2.Constant.ProductTemplate.GRID
import com.tokopedia.discovery2.Constant.PropertyType.TARGETING_BANNER
import com.tokopedia.discovery2.Constant.TopAdsSdk.TOP_ADS_GSLP_TDN
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.Utils.Companion.areFiltersApplied
import com.tokopedia.discovery2.Utils.Companion.getElapsedTime
import com.tokopedia.discovery2.Utils.Companion.isOldProductCardType
import com.tokopedia.discovery2.analytics.EMPTY_STRING
import com.tokopedia.discovery2.data.AdditionalInfo
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.data.ErrorState.NetworkErrorState
import com.tokopedia.discovery2.data.PageInfo
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.data.automatecoupon.Layout
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.ACTIVE_TAB
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.CATEGORY_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.FORCED_NAVIGATION
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.QUERY_PARENT
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.RECOM_PRODUCT_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.TARGET_COMP_ID
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview.AutoPlayController
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.asCamelCase
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartItemType
import com.tokopedia.minicart.common.domain.data.getMiniCartItemParentProduct
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import java.util.*
import kotlin.collections.set

val discoveryPageData: MutableMap<String, DiscoveryResponse> = HashMap()
const val DYNAMIC_COMPONENT_IDENTIFIER = "dynamic_"
const val SHIMMER_ITEMS_LIST_SIZE = 10
const val COMPONENTS_PER_PAGE = 10
var discoComponentQuery: MutableMap<String, String?>? = null

fun mapDiscoveryResponseToPageData(
    discoveryResponse: DiscoveryResponse,
    queryParameterMap: MutableMap<String, String?>,
    userAddressData: LocalCacheModel?,
    isLoggedIn: Boolean,
    shouldHideSingleProdCard: Boolean
): DiscoveryPageData {
    val pageInfo = discoveryResponse.pageInfo
    val discoveryPageData = DiscoveryPageData(pageInfo, discoveryResponse.additionalInfo)
    discoComponentQuery = queryParameterMap
    val discoveryDataMapper = DiscoveryPageDataMapper(
        pageInfo,
        queryParameterMap,
        discoveryResponse.queryParamMapWithRpc,
        discoveryResponse.queryParamMapWithoutRpc,
        userAddressData,
        isLoggedIn,
        shouldHideSingleProdCard
    )
    if (discoveryResponse.components.isNotEmpty()) {
        discoveryPageData.components = discoveryDataMapper.getDiscoveryComponentListWithQueryParam(
            discoveryResponse.components.filter {
                pageInfo.identifier?.let { identifier ->
                    it.pageEndPoint = identifier
                }
                pageInfo.path?.let { path ->
                    it.pagePath = path
                }
                discoveryResponse.componentMap[it.id] = it
                it.renderByDefault
            }
        )
    }
    discoveryResponse.component?.setComponentsItem(discoveryResponse.components)
    return discoveryPageData
}

class DiscoveryPageDataMapper(
    private val pageInfo: PageInfo,
    private val queryParameterMap: MutableMap<String, String?>,
    private val queryParameterMapWithRpc: Map<String, String>,
    private val queryParameterMapWithoutRpc: Map<String, String>,
    private val localCacheModel: LocalCacheModel?,
    private val isLoggedIn: Boolean,
    private val shouldHideSingleProdCard: Boolean
) {
    fun getDiscoveryComponentListWithQueryParam(components: List<ComponentsItem>): List<ComponentsItem> {
        val targetCompId = queryParameterMap[TARGET_COMP_ID] ?: ""
        val componentList = getDiscoComponentListFromResponse(components)
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
            ComponentNames.Tabs.componentName,
            ComponentNames.TabsIcon.componentName,
            ComponentNames.PlainTab.componentName,
            ComponentNames.FlashSaleTokoTab.componentName -> listComponents.addAll(
                parseTab(component, position)
            )

            ComponentNames.ProductCardRevamp.componentName,
            ComponentNames.ProductCardSprintSale.componentName -> {
                addRecomQueryProdID(component)
                listComponents.addAll(parseProductVerticalList(component))
            }

            ComponentNames.BannerInfinite.componentName -> listComponents.addAll(
                parseProductVerticalList(component, false)
            )

            ComponentNames.ShopCardInfinite.componentName -> listComponents.addAll(
                parseProductVerticalList(component, component.areFiltersApplied())
            )

            ComponentNames.ProductCardSprintSaleCarousel.componentName,
            ComponentNames.ProductCardCarousel.componentName -> {
                addRecomQueryProdID(component)
                updateCarouselWithCart(component)
                listComponents.add(component)
            }

            ComponentNames.Section.componentName -> {
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
                handleQuickFilter(component)
            }

            ComponentNames.CalendarWidgetGrid.componentName,
            ComponentNames.CalendarWidgetCarousel.componentName -> {
                listComponents.add(component)
                if (component.properties?.calendarType.equals(DYNAMIC) &&
                    component.properties?.calendarLayout.equals(GRID)
                ) {
                    listComponents.addAll(parseProductVerticalList(component, false))
                } else if (component.properties?.calendarType == STATIC) {
                    if (component.getComponentsItem().isNullOrEmpty()) {
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
                    if (component.properties?.calendarLayout.equals(GRID)) {
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
                if (isLoggedIn) {
                    listComponents.addAll(setupMerchantVoucherList(component))
                }
            }

            ComponentNames.ProductCardSingle.componentName -> {
                if (component.properties.isOldProductCardType()) {
                    if (!shouldHideSingleProdCard) {
                        addRecomQueryProdID(component)
                        listComponents.add(component)
                    }
                } else {
                    if (!shouldHideSingleProdCard) {
                        component.name = ComponentNames.ProductCardSingleReimagine.componentName
                        addRecomQueryProdID(component)
                        listComponents.add(component)
                    }
                }
            }

            ComponentNames.ExplicitWidget.componentName -> {
                addPageInfoToExplicitWidget(component)
                listComponents.add(component)
            }

            ComponentNames.SliderBanner.componentName -> {
                listComponents.addAll(
                    getTargetingBannerParsed(
                        component = component,
                        position = position
                    )
                )
            }

            ComponentNames.AutomateCoupon.componentName -> {
                parseAutomateCoupon(component, listComponents)
            }

            else -> listComponents.add(component)
        }
        return listComponents
    }

    private fun getFiltersFromQuery(component: ComponentsItem, queryParameterMapWithRpc: Map<String, String>) {
        for ((key, value) in queryParameterMapWithRpc) {
            val adjustedValue = Utils.isRPCFilterApplicableForTab(value, component)
            if (adjustedValue.isNotEmpty()) {
                component.searchParameter.set(key, adjustedValue)
            }
        }
    }

    private fun addRecomQueryProdID(component: ComponentsItem) {
        if (!queryParameterMap[RECOM_PRODUCT_ID].isNullOrEmpty()) {
            component.recomQueryProdId = queryParameterMap[RECOM_PRODUCT_ID]
        }
    }

    private fun saveSectionPosition(pageEndPoint: String, sectionId: String, position: Int) {
        if (getSectionPositionMap(pageEndPoint) == null) {
            setSectionPositionMap(mutableMapOf(), pageEndPoint)
        }
        getSectionPositionMap(pageEndPoint)?.put(sectionId, position)
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
                ComponentsItem().apply { autoPlayController = component.autoPlayController }
            )
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
        component.properties ?: kotlin.run {
            component.properties = Properties()
        }
        component.properties?.template = Constant.ProductTemplate.LIST
        component.componentsPerPage = COMPONENTS_PER_PAGE
        return parseProductVerticalList(component, component.areFiltersApplied())
    }

    private fun addBannerTimerComp(component: ComponentsItem): Boolean {
        if (component.data?.firstOrNull()?.endDate.isNullOrEmpty() || component.data?.firstOrNull()?.startDate.isNullOrEmpty()) {
            return false
        }
        return getElapsedTime(component.data?.firstOrNull()?.endDate ?: "") > 0
    }

    private fun addPageInfoToExplicitWidget(component: ComponentsItem) {
        component.pageType = pageInfo.type ?: EMPTY_STRING
    }

    private fun getTargetingBannerParsed(
        component: ComponentsItem,
        position: Int
    ): List<ComponentsItem> {
        val listComponents: ArrayList<ComponentsItem> = ArrayList()
        // set components item if components item is null
        setComponentsItemTargetingBanner(
            component = component
        )
        // add component
        listComponents.add(component)
        // add all items based on component item
        listComponents.addAll(
            getComponentsItemListTargetingBannerParsed(
                component = component,
                position = position,
                totalData = listComponents.size
            )
        )
        return listComponents
    }

    private fun setComponentsItemTargetingBanner(
        component: ComponentsItem
    ) {
        if (component.getComponentsItem().isNullOrEmpty() &&
            component.properties?.type == TARGETING_BANNER
        ) {
            component.setComponentsItem(
                listComponents = DiscoveryDataMapper().mapListToComponentList(
                    itemList = component.data.orEmpty(),
                    subComponentName = component.name.orEmpty(),
                    properties = component.properties,
                    creativeName = component.creativeName,
                    parentComponentPosition = component.position,
                    parentSectionId = component.parentSectionId
                )
            )
        }
    }

    private fun getComponentsItemListTargetingBannerParsed(
        component: ComponentsItem,
        position: Int,
        totalData: Int
    ): List<ComponentsItem> {
        component.getComponentsItem()?.forEachIndexed { index, componentItem ->
            componentItem.data?.firstOrNull()?.let { dataItem ->
                if (component.itemPosition == index || component.isFirstShown) {
                    component.isFirstShown = false
                    dataItem.targetComponentIds.forEach { id ->
                        getComponent(
                            componentId = id.toString(),
                            pageName = pageInfo.identifier.orEmpty()
                        )?.let { component ->
                            return parseComponent(
                                component = component,
                                position = position + totalData
                            )
                        }
                    }
                }
            }
        }
        return emptyList()
    }

    private fun parseTab(component: ComponentsItem, position: Int): List<ComponentsItem> {
        val isDynamicTabs = component.properties?.dynamic ?: false

        component.apply {
            pinnedActiveTabId = queryParameterMapWithoutRpc[ACTIVE_TAB]
            parentComponentPosition = position
        }

        if (!isDynamicTabs && component.data.isNullOrEmpty()) return emptyList()

        val listComponents: ArrayList<ComponentsItem> = ArrayList()

        val hasPlainBackground = component.properties?.background == TAB_DEFAULT_BACKGROUND
        if (component.name == ComponentNames.Tabs.componentName && hasPlainBackground) {
            component.name = ComponentNames.PlainTab.componentName
        }

        listComponents.add(component)

        if (component.getComponentsItem().isNullOrEmpty()) {
            component.setComponentsItem(
                DiscoveryDataMapper.mapTabsListToComponentList(
                    component,
                    component.name ?: ComponentNames.TabsItem.componentName
                ),
                component.tabName
            )
        } else {
            // this is for the forced redirection case only, whenever tabs position is change using
            // the product click from one tab to other
            val isForcedRedirection = !component.getComponentsItem().isNullOrEmpty() &&
                queryParameterMap[FORCED_NAVIGATION] == "true"

            if (isForcedRedirection) {
                val activeTabIndex = queryParameterMapWithoutRpc[ACTIVE_TAB]?.toIntOrNull()
                if (activeTabIndex != null) {
                    component.getComponentsItem()?.forEachIndexed { index, componentItem ->
                        Utils.setTabSelectedBasedOnDataItem(
                            componentItem,
                            activeTabIndex == index + 1
                        )
                    }
                }
                queryParameterMap.remove(FORCED_NAVIGATION)
                component.shouldRefreshComponent = true
            }
        }

        component.getComponentsItem()?.forEachIndexed { index, componentsItem ->
            val data = componentsItem.data

            if (data.isNullOrEmpty() || !data.first().isSelected) return@forEachIndexed

            val tabData = data.first()

            val tabIdentifier = generateTabIdentifier(componentsItem, index)

            val targetComponentIdList = tabData
                .targetComponentId
                ?.split(",")
                ?.map {
                    if (isDynamicTabs) {
                        DYNAMIC_COMPONENT_IDENTIFIER + tabIdentifier + it.trim()
                    } else {
                        it.trim()
                    }
                }

            if (targetComponentIdList.isNullOrEmpty()) return@forEachIndexed

            val tabsChildComponentsItemList: ArrayList<ComponentsItem> = ArrayList()

            targetComponentIdList.forEach { componentId ->
                val tabComponent = if (isDynamicTabs) {
                    handleDynamicTabsComponents(componentId, tabIdentifier, component, tabData.name)
                } else {
                    handleAvailableComponents(componentId, component, tabData.name)
                }

                tabComponent?.let {
                    tabsChildComponentsItemList.add(it)
                    it.tabPosition = index
                    val addPosition = position + listComponents.size

                    listComponents.addAll(parseComponent(it, addPosition))
                }
            }

            componentsItem.setComponentsItem(tabsChildComponentsItemList, tabData.name)
        }

        return listComponents
    }

    private fun generateTabIdentifier(
        componentsItem: ComponentsItem,
        index: Int
    ): String {
        val isFlashSaleToko = componentsItem.name == ComponentNames.FlashSaleTokoTab.componentName

        return if (isFlashSaleToko) {
            componentsItem.data?.first()?.filterValue ?: index.toString()
        } else {
            index.toString()
        }
    }

    private fun handleDynamicTabsComponents(
        targetedComponentId: String,
        tabItemIndex: String,
        tabComponent: ComponentsItem,
        tabName: String?
    ): ComponentsItem? {
        var tabChildComponentsItem: ComponentsItem? = null
        val pageIdentity = pageInfo.identifier.orEmpty()
        val originalComponentId = targetedComponentId
            .removePrefix("$DYNAMIC_COMPONENT_IDENTIFIER$tabItemIndex")

        val targetComponent = getComponent(targetedComponentId, pageIdentity)

        if (targetComponent == null) {
            val originalComponent = getComponent(originalComponentId, pageIdentity)

            originalComponent?.let { component ->
                component.copy().apply {
                    parentComponentId = tabComponent.id
                    id = targetedComponentId
                    this.tabName = tabName
                    dynamicOriginalId = originalComponentId
                    filterController = FilterController()
                    properties = component.properties
                    properties?.dynamic = tabComponent.properties?.dynamic ?: false
                    setComponent(targetedComponentId, pageIdentity, this)

                    tabChildComponentsItem = this
                }
            }
        } else {
            tabChildComponentsItem = handleAvailableComponents(
                targetedComponentId,
                tabComponent,
                tabName
            )
        }

        return tabChildComponentsItem
    }

    private fun handleAvailableComponents(
        targetedComponentId: String,
        tabComponent: ComponentsItem,
        tabName: String?
    ): ComponentsItem? {
        val pageIdentity = pageInfo.identifier ?: ""
        var tabChildComponentsItem: ComponentsItem? = null
        getComponent(targetedComponentId, pageIdentity)?.let { component1 ->
            component1.parentComponentId = tabComponent.id
            component1.tabName = tabName
            tabChildComponentsItem = component1
        }
        return tabChildComponentsItem
    }

    private fun parseProductVerticalList(
        component: ComponentsItem,
        showEmptyState: Boolean = true
    ): List<ComponentsItem> {
        val listComponents: LinkedList<ComponentsItem> = LinkedList()

        if (component.verticalProductFailState) {
            listComponents.add(component)
            component.getComponentsItem()?.let {
                listComponents.addAll(getDiscoveryComponentList(it))
            }
            when (component.errorState) {
                NetworkErrorState -> {
                    listComponents.addAll(
                        handleProductState(
                            component,
                            ComponentNames.ProductListNetworkErrorLoad.componentName,
                            queryParameterMap
                        )
                    )
                }

                else -> {
                    listComponents.addAll(
                        handleProductState(
                            component,
                            ComponentNames.ProductListErrorLoad.componentName,
                            queryParameterMap
                        )
                    )
                }
            }
        } else {
            if (component.getComponentsItem().isNullOrEmpty() &&
                component.noOfPagesLoaded == 0 &&
                !component.verticalProductFailState
            ) {
                listComponents.add(
                    component.copy().apply {
                        setComponentsItem(component.getComponentsItem(), component.tabName)
                    }
                )
                component.needPagination = true
                component.userAddressData = localCacheModel
                listComponents.addAll(
                    List(SHIMMER_ITEMS_LIST_SIZE) {
                        ComponentsItem(
                            name =
                            if (component.name == ComponentNames.CalendarWidgetGrid.componentName) {
                                ComponentNames.ShimmerCalendarWidget.componentName
                            } else {
                                ComponentNames.ShimmerProductCard.componentName
                            }
                        ).apply {
                            properties = component.properties
                            parentComponentName = component.name
                        }
                    }
                )
            } else {
                listComponents.add(component)
                component.getComponentsItem()?.let {
                    listComponents.addAll(
                        getDiscoveryComponentList(it).apply {
                            if (component.properties?.tokonowATCActive == true) {
                                updateWithCart(it, getCartData(component.pageEndPoint))
                            }
                        }
                    )
                }
                if (component.properties?.index != null &&
                    component.properties?.index!! > Int.ZERO &&
                    component.properties?.index!! < listComponents.size &&
                    !component.properties?.targetedComponentId.isNullOrEmpty()
                ) {
                    getComponent(
                        component.properties?.targetedComponentId!!,
                        component.pageEndPoint
                    )?.let {
                        if (it.name == ComponentNames.DiscoTDNBanner.componentName) {
                            it.design = TOP_ADS_GSLP_TDN
                            it.recomQueryProdId = component.recomQueryProdId
                        }
                        listComponents.add(component.properties?.index!! + Int.ONE, it)
                    }
                }

                if (Utils.nextPageAvailable(
                        component,
                        component.componentsPerPage
                    ) && component.showVerticalLoader
                ) {
                    listComponents.addAll(
                        handleProductState(
                            component,
                            ComponentNames.LoadMore.componentName,
                            queryParameterMap
                        )
                    )
                } else if (component.getComponentsItem()?.size == 0 && showEmptyState) {
                    listComponents.addAll(
                        handleProductState(
                            component,
                            ComponentNames.ProductListEmptyState.componentName,
                            queryParameterMap
                        )
                    )
                }
            }
        }
        return listComponents
    }

    private fun handleProductState(
        component: ComponentsItem,
        componentName: String,
        queryParameterMap: Map<String, String?>? = null
    ): ArrayList<ComponentsItem> {
        val productState: ArrayList<ComponentsItem> = ArrayList()
        productState.add(
            ComponentsItem(name = componentName).apply {
                pageEndPoint = component.pageEndPoint
                parentComponentId = component.id
                parentComponentName = component.name
                rpc_discoQuery = queryParameterMap
                id = componentName
                discoveryPageData[this.pageEndPoint]?.componentMap?.set(this.id, this)
            }
        )
        return productState
    }

    private fun updateCarouselWithCart(component: ComponentsItem) {
        if (component.properties?.tokonowATCActive == true) {
            component.getComponentsItem()?.let {
                if (updateWithCart(it, getCartData(component.pageEndPoint))) {
                    component.shouldRefreshComponent = true
                }
            }
        }
    }

    private fun updateWithCart(
        list: List<ComponentsItem>,
        map: Map<MiniCartItemKey, MiniCartItem>?
    ): Boolean {
        if (map == null) return false

        var shouldRefresh = false

        list.forEach { item ->
            item.data?.firstOrNull()?.let { dataItem ->
                if (dataItem.hasATC && !dataItem.parentProductId.isNullOrEmpty() && map.containsKey(
                        MiniCartItemKey(
                                dataItem.parentProductId ?: "",
                                type = MiniCartItemType.PARENT
                            )
                    )
                ) {
                    map.getMiniCartItemParentProduct(
                        dataItem.parentProductId ?: ""
                    )?.totalQuantity?.let { quantity ->
                        if (updateQuantity(quantity, item)) {
                            shouldRefresh = true
                        }
                    }
                } else if (dataItem.hasATC && !dataItem.productId.isNullOrEmpty() && map.containsKey(
                        MiniCartItemKey(dataItem.productId ?: "")
                    )
                ) {
                    map.getMiniCartItemProduct(
                        dataItem.productId ?: ""
                    )?.quantity?.let { quantity ->
                        if (updateQuantity(quantity, item)) {
                            shouldRefresh = true
                        }
                    }
                } else {
                    if (updateQuantity(0, item)) {
                        shouldRefresh = true
                    }
                }
            }
        }
        return shouldRefresh
    }

    private fun updateQuantity(quantity: Int, item: ComponentsItem): Boolean {
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

        val componentsItem = component.getComponentsItem()

        val shouldSupportFestive = componentsItem?.find { !it.isBackgroundPresent } == null

        markTargetedFST(componentsItem)

        if (!shouldSupportFestive) {
            componentsItem?.let {
                listComponents.addAll(
                    getSectionComponentList(it.filter { !it.isTargetedTabComponent }, component.position + 1)
                )
            }
        } else {
            val updatedComponents = parseSectionChildren(componentsItem)

            if (updatedComponents.isNotEmpty()) {
                listComponents.first().setComponentsItem(updatedComponents)
                overwriteFlashSaleTabParentCompId(listComponents)
            }
        }

        return listComponents
    }

    private fun overwriteFlashSaleTabParentCompId(listComponents: ArrayList<ComponentsItem>) {
        val targetedIds = listComponents.first().getComponentsItem()
            ?.filter {
                it.name == ComponentNames.FlashSaleTokoTab.componentName
            }
            ?.map { it.data?.firstOrNull()?.targetComponentId.orEmpty() to it.id }
            ?.filter { it.first.isNotEmpty() }

        targetedIds?.forEach { (targetComponentId, tabId) ->
            listComponents.first().getComponentsItem()
                ?.find { it.isTargetedTabComponent && it.dynamicOriginalId == targetComponentId }
                ?.parentComponentId = tabId
        }
    }

    private fun markTargetedFST(componentsItem: List<ComponentsItem>?) {
        val flashSaleTab = componentsItem
            ?.find {
                it.name == ComponentNames.FlashSaleTokoTab.componentName
            }

        if (flashSaleTab != null) {
            val targetedComponentId = flashSaleTab.data?.firstOrNull()?.targetComponentId.orEmpty()
            val index = componentsItem.indexOfFirst {
                it.name == ComponentNames.ProductCardCarousel.componentName &&
                    (it.id == targetedComponentId || it.dynamicOriginalId == targetedComponentId)
            }

            if (index != -1) {
                componentsItem[index].isTargetedTabComponent = true
            }
        }
    }

    private fun parseSectionChildren(components: List<ComponentsItem>?): List<ComponentsItem> {
        val nonTargetedTabComponent = components?.filter { !it.isTargetedTabComponent }
        val parsedChildrenComponent = nonTargetedTabComponent.orEmpty().toMutableList()

        if (parsedChildrenComponent.hasFlashSaleTab()) {
            val parsedComponent = parseFestiveFlashSaleTab(parsedChildrenComponent)
            parsedChildrenComponent.clear()
            parsedChildrenComponent.addAll(parsedComponent)
        }

        if (parsedChildrenComponent.hasAutomateCoupon()) {
            val parsedComponent = parseFestiveAutomateCoupon(parsedChildrenComponent)
            parsedChildrenComponent.clear()
            parsedChildrenComponent.addAll(parsedComponent)
        }

        return parsedChildrenComponent
    }

    private fun List<ComponentsItem>?.hasFlashSaleTab(): Boolean {
        return this?.find {
                it.name == ComponentNames.FlashSaleTokoTab.componentName
            } != null
    }

    private fun List<ComponentsItem>?.hasAutomateCoupon(): Boolean {
        return this?.find {
            it.name == ComponentNames.AutomateCoupon.componentName
        } != null
    }

    private fun parseFestiveFlashSaleTab(componentsItem: List<ComponentsItem>?): List<ComponentsItem> {
        val flashSaleTab = componentsItem
            ?.find {
                it.name == ComponentNames.FlashSaleTokoTab.componentName
            }

        val updatedComponentItems = mutableListOf<ComponentsItem>()

        flashSaleTab?.let {
            val parsedTab = parseTab(it, it.position)

            componentsItem.forEach { component ->
                val isFSTComponent = component.name == ComponentNames.FlashSaleTokoTab.componentName

                when {
                    isFSTComponent -> {
                        updatedComponentItems.addAll(parsedTab)
                    }
                    else -> {
                        updatedComponentItems.add(component)
                    }
                }
            }
        } ?: run {
            updatedComponentItems.addAll(componentsItem.orEmpty())
        }

        return updatedComponentItems
    }

    private fun parseFestiveAutomateCoupon(componentsItem: List<ComponentsItem>?): List<ComponentsItem> {
        val updatedComponentItems = arrayListOf<ComponentsItem>()

        componentsItem?.forEach {
            if (it.name == ComponentNames.AutomateCoupon.componentName) {
                parseAutomateCoupon(it, updatedComponentItems)
            } else {
                updatedComponentItems.add(it)
            }
        }

        return updatedComponentItems
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

    private fun handleQuickFilter(component: ComponentsItem) {
        component.isSticky = component.properties?.chipSize == Constant.ChipSize.LARGE

        val isQueryParameterAvailable = queryParameterMapWithRpc.isNotEmpty() || queryParameterMapWithoutRpc.isNotEmpty()
        if (!component.isSelectedFiltersFromQueryApplied && isQueryParameterAvailable) {
            component.isSelectedFiltersFromQueryApplied = true
            getFiltersFromQuery(
                component,
                queryParameterMapWithRpc + queryParameterMapWithoutRpc
            )
        }

        Utils.getTargetComponentOfFilter(component)?.let {
            val parameterMap = component.searchParameter.getSearchParameterHashMap()

            if (it.selectedFilters.isNullOrEmpty() && parameterMap.isNotEmpty()) {
                it.selectedFilters = parameterMap
            }
        }

        component.properties?.targetId?.let {
            getComponent(it, component.pageEndPoint)?.apply {
                parentFilterComponentId = component.id
            }
        }

        addQueryParentOnSearchParameter(component)
    }

    private fun addQueryParentOnSearchParameter(component: ComponentsItem) {
        val query = discoComponentQuery?.get(QUERY_PARENT)

        val existingParams = component.searchParameter.getSearchParameterMap()

        val parameter = SearchParameterFactory
            .withExistingParameter(existingParams)
            .construct(query, component.pagePath)

        parameter?.run { component.searchParameter = SearchParameter(this) }
    }

    private fun parseAutomateCoupon(
        component: ComponentsItem,
        listComponents: ArrayList<ComponentsItem>
    ) {
        val layout = component.data?.firstOrNull()?.couponLayout
        layout?.let {
            Layout.valueOf(it.asCamelCase())
            val componentName = when (Layout.valueOf(it.asCamelCase())) {
                Layout.Single -> ComponentNames.SingleAutomateCoupon.componentName
                Layout.Double -> ComponentNames.GridAutomateCoupon.componentName
                Layout.Carousel -> ComponentNames.CarouselAutomateCoupon.componentName
            }

            val uniqueId = "${it}_${component.id}"
            val parsedComponent = component.copy(
                id = uniqueId,
                name = componentName,
                parentComponentName = ComponentNames.AutomateCoupon.componentName
            )
            listComponents.add(parsedComponent)
            setComponent(uniqueId, component.pageEndPoint, parsedComponent)
        }
    }

    companion object {
        private const val TAB_DEFAULT_BACKGROUND = "plain"
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

fun getCartData(pageName: String): Map<MiniCartItemKey, MiniCartItem>? {
    discoveryPageData[pageName]?.let {
        return it.cartMap
    }
    return null
}

fun setCartData(cartMap: Map<MiniCartItemKey, MiniCartItem>, pageName: String) {
    discoveryPageData[pageName]?.let {
        it.cartMap = cartMap
    }
}

fun setSectionPositionMap(map: MutableMap<String, Int>, pageName: String) {
    discoveryPageData[pageName]?.let {
        it.sectionMap = map
    }
}

fun getSectionPositionMap(pageName: String): MutableMap<String, Int>? {
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

fun getAdditionalInfo(pageName: String): AdditionalInfo? {
    return discoveryPageData[pageName]?.additionalInfo
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
