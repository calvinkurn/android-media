package com.tokopedia.discovery2.datamapper

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper

fun mapDiscoveryResponseToPageData(discoveryResponse: DiscoveryResponse): DiscoveryPageData {
    val discoveryPageData = DiscoveryPageData(discoveryResponse.pageInfo, discoveryResponse.title)
    discoveryResponse.apply {
        val renderdByDefaultFalseComponentMap: MutableMap<String, ComponentsItem> = HashMap()

        val listComponents: ArrayList<ComponentsItem> = ArrayList()

        components.forEach {
            renderdByDefaultFalseComponentMap[it.id] = it
        }
        for ((position,component) in components.withIndex()) {

            if (!component.renderByDefault) {
                continue
            }
            if(component.name == "tabs") {
                component.data?.let {
                    component.componentsItem = DiscoveryDataMapper.mapTabsListToComponentList(it, ComponentNames.TabsItem.componentName, position)
                }
                component.componentsItem?.forEach {
                    it.apply {
                        val selectedTabData = data?.get(0);
                        if (!selectedTabData?.targetComponentId.isNullOrEmpty()) {
                            val targetComponentIdList = selectedTabData?.targetComponentId?.split(",")?.map { it.trim() }
                            if (!targetComponentIdList.isNullOrEmpty()) {
                                val componentsItem:ArrayList<ComponentsItem> = ArrayList()
                                targetComponentIdList.forEach { componentId ->
                                    renderdByDefaultFalseComponentMap[componentId]?.let { component ->
                                        componentsItem.add(component)
                                    }
                                }
                                this.componentsItem = componentsItem
                            }
                        }
                    }

                }
            }
            listComponents.add(component)
        }

        discoveryPageData.components.add(listComponents)

    }
    return discoveryPageData
}