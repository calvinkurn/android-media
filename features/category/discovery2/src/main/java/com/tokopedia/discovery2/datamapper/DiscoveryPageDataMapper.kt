package com.tokopedia.discovery2.datamapper

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper

val componentMap: MutableMap<String, ComponentsItem> = HashMap()
fun mapDiscoveryResponseToPageData(discoveryResponse: DiscoveryResponse): DiscoveryPageData {
    val discoveryPageData = DiscoveryPageData(discoveryResponse.pageInfo, discoveryResponse.title)
    discoveryResponse.components.filter {
        componentMap[it.id] = it
        it.renderByDefault
    }
    discoveryPageData.components = getDiscvoeryComponentList(discoveryResponse.components)
    return discoveryPageData
}

fun getDiscvoeryComponentList(components: List<ComponentsItem>): List<ComponentsItem> {
    var listComponents: ArrayList<ComponentsItem> = ArrayList()
    for ((position, component) in components.withIndex()) {
        listComponents.add(component)
        if (component.name == "tabs") {
            component.data?.let {
                component.componentsItem = DiscoveryDataMapper.mapTabsListToComponentList(it, ComponentNames.TabsItem.componentName, position)
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
                                componentMap[componentId]?.let { component ->
                                    componentsItem.add(component)
                                }
                            }
                            this.componentsItem = componentsItem
                        }
                    }
                }
            }
            selectedTab?.componentsItem?.let {
                listComponents.addAll(getDiscvoeryComponentList(it))
            }
        } else if (component.name == "product_card_revamp") {
            if (component.componentsItem.isNullOrEmpty()) {
                listComponents.addAll(List(10) { ComponentsItem(name = "shimmer_product_card") })
            }else{
                component.componentsItem?.let {
                    listComponents.addAll(getDiscvoeryComponentList(it))
                }
            }
        }

    }
    return listComponents
}
