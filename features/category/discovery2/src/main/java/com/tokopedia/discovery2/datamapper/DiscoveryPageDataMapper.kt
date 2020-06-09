package com.tokopedia.discovery2.datamapper

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.data.PageInfo
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper


val discoveryPageData: MutableMap<String, DiscoveryResponse> = HashMap()

fun mapDiscoveryResponseToPageData(discoveryResponse: DiscoveryResponse): DiscoveryPageData {
    discoveryResponse.pageInfo.name = discoveryResponse.title
    val discoveryPageData = DiscoveryPageData(discoveryResponse.pageInfo, discoveryResponse.title)
    discoveryResponse.componentMap = HashMap()
    discoveryPageData.components = getDiscvoeryComponentList(discoveryResponse.pageInfo, discoveryResponse.components.filter {
        discoveryPageData.pageInfo.identifier?.let { identifier ->
            it.pageEndPoint = identifier
        }
        discoveryResponse.componentMap[it.id] = it
        it.renderByDefault
    })
    return discoveryPageData
}

fun getDiscvoeryComponentList(pageInfo: PageInfo, components: List<ComponentsItem>): List<ComponentsItem> {
    var listComponents: ArrayList<ComponentsItem> = ArrayList()
    for ((position, component) in components.withIndex()) {
        listComponents.add(component)
        if (component.name == "tabs") {
            component.data?.let { it ->
                if (component.componentsItem.isNullOrEmpty()) {
                    component.componentsItem = DiscoveryDataMapper.mapTabsListToComponentList(component, ComponentNames.TabsItem.componentName, position)
                } else {
                    //For List Adapter need to update list object
                    //Trade off between page redraw or list copy
                    //TODO try to improve this logic
                    component.componentsItem = component.componentsItem?.map { cmpt -> cmpt.copy() }
                }
            }
            var selectedTab: ComponentsItem? = null
            component.componentsItem?.forEach {

                it.apply {
                    val tabData = data?.get(0);
                    if (tabData?.isSelected!!) {
                        selectedTab = it
                    }
                    if (!tabData?.targetComponentId.isNullOrEmpty()) {
                        val targetComponentIdList = tabData?.targetComponentId?.split(",")?.map { it.trim() }
                        if (!targetComponentIdList.isNullOrEmpty()) {
                            val componentsItem: ArrayList<ComponentsItem> = ArrayList()
                            targetComponentIdList.forEach { componentId ->
                                getComponent(componentId, pageInfo.identifier!!)?.let { component ->
                                    componentsItem.add(component)
                                }
                            }
                            this.componentsItem = componentsItem
                        }
                    }
                }
            }
            selectedTab?.componentsItem?.let {
                listComponents.addAll(getDiscvoeryComponentList(pageInfo, it))
            }
        } else if (component.name == "product_card_revamp" || component.name == "product_card_sprint_sale") {
            if (component.componentsItem.isNullOrEmpty() && component.noOfPagesLoaded == 0) {
                component.needPagination = true
                listComponents.addAll(List(10) { ComponentsItem(name = "shimmer_product_card") })
            } else {
                component.componentsItem?.let {
                    listComponents.addAll(getDiscvoeryComponentList(pageInfo, it))
                }
                if (component.componentsItem?.size?.rem(component.componentsPerPage) == 0) {
                    listComponents.add(ComponentsItem(name = "load_more").apply {
                        pageEndPoint = component.pageEndPoint
                        parentComponentId = component.id
                        id = "load_more_data"
                        discoveryPageData[this.pageEndPoint]?.componentMap?.set(this.id, this)

                    })
                }
            }
        }
    }
    return listComponents
}

//else if (component.name == "product_card_revamp" || component.name == "product_card_sprint_sale") {
//    if (component.componentsItem.isNullOrEmpty() && component.noOfPagesLoaded == 0) {
//        component.needPagination = true
//        listComponents.addAll(List(10) { ComponentsItem(name = "shimmer_product_card") })
//    } else if (component.componentsItem?.size?.rem(component.componentsPerPage) == 0) {
//        component.componentsItem?.let {
//            listComponents.addAll(getDiscvoeryComponentList(pageInfo, it))
//            listComponents.add(ComponentsItem(name = "load_more"))
//        }
//    } else {
//        component.componentsItem?.let {
//            listComponents.addAll(getDiscvoeryComponentList(pageInfo, it))
//        }
//    }
//}


fun getComponent(componentId: String, pageName: String): ComponentsItem? {
    discoveryPageData[pageName].let {
        return it?.componentMap?.get(componentId)
    }
}