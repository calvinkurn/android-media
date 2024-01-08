package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopoffersupportingbrand

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.discoveryPageData

object ShopOfferSupportingBrandComponentExtension {
    fun ArrayList<ComponentsItem>.addLoadMore(
        component: ComponentsItem
    ) {
        val loadMore = ComponentsItem(
            name = ComponentNames.LoadMore.componentName
        ).apply {
            pageEndPoint = component.pageEndPoint
            parentComponentId = component.id
            id = ComponentNames.LoadMore.componentName
            loadForHorizontal = true
            discoveryPageData[this.pageEndPoint]?.componentMap?.set(this.id, this)
        }
        add(loadMore)
    }

    fun ArrayList<ComponentsItem>.addReload(
        component: ComponentsItem
    ) {
        val reload = ComponentsItem(
            name = ComponentNames.CarouselErrorLoad.componentName
        ).apply {
            pageEndPoint = component.pageEndPoint
            parentComponentId = component.id
            id = ComponentNames.CarouselErrorLoad.componentName
            parentComponentPosition = component.position
            discoveryPageData[this.pageEndPoint]?.componentMap?.set(this.id, this)
        }
        add(reload)
    }
}
