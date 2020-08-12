package com.tokopedia.discovery2.usecase.productCardCarouselUseCase

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import javax.inject.Inject

class ProductCardItemUseCase @Inject constructor() {
    private var shouldReSync: Boolean = false
    fun notifyProductComponentUpdate(componentID: String, pageEndPoint: String): Boolean {
        shouldReSync = false
        val productComponent = getComponent(componentID, pageEndPoint)
        val preSelectedTabItemId = productComponent?.parentComponentId

        preSelectedTabItemId?.let { tabItemId ->
            val tabItemComponent = getComponent(tabItemId, pageEndPoint)
            tabItemComponent?.let { tabItem ->
                val tabComponent = getComponent(tabItem.parentComponentId, pageEndPoint)
                tabComponent?.let { tabItem ->
                    val parentTab = getComponent(tabItem.parentComponentId, pageEndPoint)
                    if (parentTab?.getComponentsItem()?.size.isMoreThanZero()) {
                        parentTab?.getComponentsItem()?.forEach { item ->

                            if (item.getComponentsItem()?.size.isMoreThanZero()) {
                                item.getComponentsItem()?.forEach { tabSubComps ->
                                    reInitialiseTabComponents(tabSubComps)
                                }
                            }
                        }
                    }
                }
            }
        }
        return shouldReSync
    }

    private fun reInitialiseTabComponents(componentsItem: ComponentsItem) {
        if (componentsItem.name == ComponentNames.ProductCardSprintSale.componentName
                || componentsItem.name == ComponentNames.ProductCardSprintSaleCarousel.componentName) {
            shouldReSync = true
            componentsItem.setComponentsItem(null)
            componentsItem.noOfPagesLoaded = 0
        }
    }

}