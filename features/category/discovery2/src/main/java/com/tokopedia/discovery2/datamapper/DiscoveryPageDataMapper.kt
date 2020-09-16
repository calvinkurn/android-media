package com.tokopedia.discovery2.datamapper

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.data.PageInfo
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.PINNED_ACTIVE_TAB
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.PINNED_COMP_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.PRODUCT_ID
import com.tokopedia.kotlin.extensions.view.isMoreThanZero


val discoveryPageData: MutableMap<String, DiscoveryResponse> = HashMap()

fun mapDiscoveryResponseToPageData(discoveryResponse: DiscoveryResponse, queryParameterMap: Map<String, String?>): DiscoveryPageData {
    val pageInfo = discoveryResponse.pageInfo
    val discoveryPageData = DiscoveryPageData(pageInfo, discoveryResponse.additionalInfo)
    val discoveryDataMapper = DiscoveryPageDataMapper(pageInfo, queryParameterMap)
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
    return discoveryPageData
}

class DiscoveryPageDataMapper(private val pageInfo: PageInfo, private val queryParameterMap: Map<String, String?>) {

    fun getDiscoveryComponentListWithQueryParam(components: List<ComponentsItem>): List<ComponentsItem> {
        val pinnedCompId = queryParameterMap[PINNED_COMP_ID]
        val componentList = getDiscoveryComponentList(components)
        if (componentList.isNotEmpty() && !pinnedCompId.isNullOrEmpty()) {
            componentList.forEach { item ->
                if(item.id == pinnedCompId){
                    item.rpc_PinnedProduct= queryParameterMap[PRODUCT_ID]
                }
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
            ComponentNames.SingleBanner.componentName, ComponentNames.DoubleBanner.componentName,
            ComponentNames.TripleBanner.name, ComponentNames.QuadrupleBanner.componentName -> listComponents.add(DiscoveryDataMapper.mapBannerComponentData(component))
            else -> listComponents.add(component)
        }
        return listComponents
    }


    private fun parseTab(component: ComponentsItem, position: Int): List<ComponentsItem> {
        val listComponents: ArrayList<ComponentsItem> = ArrayList()
        when {
            component.properties != null && component.properties!!.dynamic -> {
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
            component.setComponentsItem(DiscoveryDataMapper.mapTabsListToComponentList(component, ComponentNames.TabsItem.componentName, position, queryParameterMap[PINNED_ACTIVE_TAB]))
        }
        component.getComponentsItem()?.forEach {
            it.apply {
                val tabData = data?.get(0)
                if (tabData?.isSelected!!) {
                    val targetComponentIdList = tabData.targetComponentId?.split(",")?.map { it.trim() }
                    if (!targetComponentIdList.isNullOrEmpty()) {
                        val componentsItem: ArrayList<ComponentsItem> = ArrayList()
                        targetComponentIdList.forEachIndexed { index, componentId ->
                            getComponent(componentId, pageInfo.identifier!!)?.let { component1 ->
                                component1.parentComponentId = component.id
                                componentsItem.add(component1)
                                listComponents.addAll(parseComponent(component1, position))
                            }
                        }
                        this.setComponentsItem(componentsItem)
                    }

                }
            }
        }
        return listComponents
    }

    private fun parseProductVerticalList(component: ComponentsItem): List<ComponentsItem> {
        val listComponents: ArrayList<ComponentsItem> = ArrayList()
        if (component.getComponentsItem().isNullOrEmpty() && component.noOfPagesLoaded == 0) {
            listComponents.add(component.copy().apply {
                setComponentsItem(component.getComponentsItem())
            })
            component.needPagination = true
            listComponents.addAll(List(10) { ComponentsItem(name = ComponentNames.ShimmerProductCard.componentName) })
        } else {
            listComponents.add(component)
            component.getComponentsItem()?.let {
                listComponents.addAll(getDiscoveryComponentList(it))
            }
            if (component.getComponentsItem()?.size.isMoreThanZero() && component.getComponentsItem()?.size?.rem(component.componentsPerPage) == 0) {
                listComponents.addAll(handleProductState(component, ComponentNames.LoadMore.componentName))
            } else if (component.getComponentsItem()?.size == 0) {
                listComponents.addAll(handleProductState(component, ComponentNames.ProductListEmptyState.componentName))
            }
        }
        return listComponents
    }

    private fun handleProductState(component: ComponentsItem, componentName: String): ArrayList<ComponentsItem> {
        val productState: ArrayList<ComponentsItem> = ArrayList()
        productState.add(ComponentsItem(name = componentName).apply {
            pageEndPoint = component.pageEndPoint
            parentComponentId = component.id
            id = componentName
            discoveryPageData[this.pageEndPoint]?.componentMap?.set(this.id, this)
        })
        return productState
    }
}

fun getComponent(componentId: String, pageName: String): ComponentsItem? {
    discoveryPageData[pageName].let {
        return it?.componentMap?.get(componentId)
    }
}