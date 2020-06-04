package com.tokopedia.discovery2.datamapper

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DiscoveryResponse

fun mapDiscoveryResponseToPageData(discoveryResponse: DiscoveryResponse): DiscoveryPageData {
    val discoveryPageData = DiscoveryPageData(discoveryResponse.pageInfo, discoveryResponse.title)
    discoveryResponse.apply {
        val renderdByDefaultFalseComponentMap: MutableMap<String, ComponentsItem> = HashMap()

        val listComponents: ArrayList<ComponentsItem> = ArrayList()

        for (component in components) {
            if (!component.renderByDefault) {
                renderdByDefaultFalseComponentMap[component.id] = component
                continue
            }
            listComponents.add(component)
        }

        discoveryPageData.components.add(listComponents)

    }
    return discoveryPageData
}