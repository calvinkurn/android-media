package com.tokopedia.discovery2.datamapper

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.data.PageInfo
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper


val discoveryPageData: MutableMap<String, DiscoveryResponse> = HashMap()

fun mapDiscoveryResponseToPageData(discoveryResponse: DiscoveryResponse): DiscoveryPageData {
    discoveryResponse.pageInfo.name = discoveryResponse.title
    val discoveryPageData = DiscoveryPageData(discoveryResponse.pageInfo, discoveryResponse.title)
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
    for ((position,  component) in components.withIndex()) {
        listComponents.add(component)
        if (component.name == "tabs") {
            component.data?.let { it ->
                if (component.getComponentsItem().isNullOrEmpty()) {
                    component.setComponentsItem(DiscoveryDataMapper.mapTabsListToComponentList(component, ComponentNames.TabsItem.componentName, position))
                } /*else {
                    //For List Adapter need to update list object
                    //Trade off between page redraw or list copy
                    //TODO try to improve this logic
                    component.setComponentsItem(component.getComponentsItem()?.map { cmpt -> cmpt.copy().apply {
                        chipSelectionData = cmpt.chipSelectionData
                        setComponentsItem(cmpt.getComponentsItem())
                        needPagination = cmpt.needPagination
                        noOfPagesLoaded  = cmpt.noOfPagesLoaded
                        pageEndPoint = cmpt.pageEndPoint
                        parentComponentId = cmpt.parentComponentId
                        cpmData = cmpt.cpmData
                        chipSelectionData = cmpt.chipSelectionData
                        chipSelectionChange = cmpt.chipSelectionChange
                    } })
                }*/
            }
            var selectedTab: ComponentsItem? = null
            component.getComponentsItem()?.forEach {

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
                                getComponent(componentId, pageInfo.identifier!!)?.let { component1 ->
                                    component1.parentComponentId = component.id
                                    componentsItem.add(component1)
                                }
                            }
                            this.setComponentsItem(componentsItem)
                        }
                    }
                }
            }
            selectedTab?.getComponentsItem()?.let {
                listComponents.addAll(getDiscvoeryComponentList(pageInfo, it))
            }
        } else if (component.name == "product_card_revamp" || component.name == "product_card_sprint_sale") {
            if (component.getComponentsItem().isNullOrEmpty() && component.noOfPagesLoaded == 0) {
                val index = listComponents.indexOf(component)
                val copyComponent =component.copy().apply {
                    chipSelectionData = component.chipSelectionData
                    setComponentsItem(component.getComponentsItem())
                    needPagination = component.needPagination
                    noOfPagesLoaded  = component.noOfPagesLoaded
                    pageEndPoint = component.pageEndPoint
                    parentComponentId = component.parentComponentId
                    cpmData = component.cpmData
                    chipSelectionData = component.chipSelectionData
                    chipSelectionChange = component.chipSelectionChange
                }
                listComponents.remove(component)
                listComponents.add(index,copyComponent)
                component.needPagination = true
                listComponents.addAll(List(10) { ComponentsItem(name = "shimmer_product_card") })
            } else {
                component.getComponentsItem()?.let {
                    listComponents.addAll(getDiscvoeryComponentList(pageInfo, it))
                }
                if (component.getComponentsItem()?.size?.rem(component.componentsPerPage) == 0) {
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


fun getComponent(componentId: String, pageName: String): ComponentsItem? {
    discoveryPageData[pageName].let {
        return it?.componentMap?.get(componentId)
    }
}