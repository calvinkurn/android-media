package com.tokopedia.discovery2.datamapper

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.data.PageInfo
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper


val discoveryPageData: MutableMap<String, DiscoveryResponse> = HashMap()

fun mapDiscoveryResponseToPageData(discoveryResponse: DiscoveryResponse): DiscoveryPageData {
    val discoveryPageData = DiscoveryPageData(discoveryResponse.pageInfo)
    val discoveryDataMapper = DiscoveryPageDataMapper(discoveryResponse.pageInfo)
    discoveryPageData.components = discoveryDataMapper.getDiscvoeryComponentList(discoveryResponse.components.filter {
        discoveryPageData.pageInfo.identifier?.let { identifier ->
            it.pageEndPoint = identifier
        }
        discoveryResponse.componentMap[it.id] = it
        it.renderByDefault
    })
    return discoveryPageData
}
class DiscoveryPageDataMapper(val pageInfo: PageInfo) {
    fun getDiscvoeryComponentList(components: List<ComponentsItem>): List<ComponentsItem> {
        var listComponents: ArrayList<ComponentsItem> = ArrayList()
        for ((position, component) in components.withIndex()) {
            listComponents.addAll(parseComponent(component,position))

        }

        return listComponents
    }
    fun parseComponent(component: ComponentsItem,position: Int):List<ComponentsItem> {
        var listComponents: ArrayList<ComponentsItem> = ArrayList()
        when (component.name) {
            ComponentNames.Tabs.componentName -> listComponents.addAll(parseTab(component, position))
            ComponentNames.ProductCardRevamp.componentName,
            ComponentNames.ProductCardSprintSale.componentName -> listComponents.addAll(parseProductVerticalList(component))
            else -> listComponents.add(component)
        }
        return listComponents
    }


    fun parseTab(component: ComponentsItem, position: Int):List<ComponentsItem> {

        val listComponents: ArrayList<ComponentsItem> = ArrayList()
        listComponents.add(component)
        component.data?.let { it ->
            if (component.getComponentsItem().isNullOrEmpty()) {
                component.setComponentsItem(DiscoveryDataMapper.mapTabsListToComponentList(component, ComponentNames.TabsItem.componentName, position))
            }
        }
        component.getComponentsItem()?.forEach {

            it.apply {
                val tabData = data?.get(0);
                if (tabData?.isSelected!!) {
                    val targetComponentIdList = tabData?.targetComponentId?.split(",")?.map { it.trim() }
                    if (!targetComponentIdList.isNullOrEmpty()) {
                        val componentsItem: ArrayList<ComponentsItem> = ArrayList()
                        targetComponentIdList.forEachIndexed { index, componentId ->
                            getComponent(componentId, pageInfo.identifier!!)?.let { component1 ->
                                component1.parentComponentId = component.id
                                componentsItem.add(component1)
                                listComponents.addAll(parseComponent(component1,position))
                            }
                        }
                        this.setComponentsItem(componentsItem)
                    }

                }
            }
        }
        return listComponents
    }

    fun parseProductVerticalList(component: ComponentsItem):List<ComponentsItem> {
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
                listComponents.addAll(getDiscvoeryComponentList(it))
            }
            if (component.getComponentsItem()?.size?.rem(component.componentsPerPage) == 0) {
                listComponents.add(ComponentsItem(name = ComponentNames.LoadMore.componentName).apply {
                    pageEndPoint = component.pageEndPoint
                    parentComponentId = component.id
                    id = "load_more_data"
                    discoveryPageData[this.pageEndPoint]?.componentMap?.set(this.id, this)

                })
            }
        }
        return listComponents
    }
}

fun getComponent(componentId: String, pageName: String): ComponentsItem? {
    discoveryPageData[pageName].let {
        return it?.componentMap?.get(componentId)
    }
}