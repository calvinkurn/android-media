package com.tokopedia.discovery2.usecase.productCardCarouselUseCase

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import javax.inject.Inject

class ProductCardItemUseCase @Inject constructor() {
    private var shouldReSync: Boolean = false
    fun notifyProductComponentUpdate(componentID: String, pageEndPoint: String, componentSync : Boolean = false) : Boolean{
        val productComponent = getComponent(componentID, pageEndPoint)
        val preSelectedTabItemId = productComponent?.parentComponentId
        preSelectedTabItemId?.let { tabItemId ->
            val tabItemComponent = getComponent(tabItemId, pageEndPoint)
            tabItemComponent?.let { tabItem ->
                val tabComponent = getComponent(tabItem.parentComponentId, pageEndPoint)
                val preSelectedTab = tabComponent?.id
                tabComponent?.let { tabItem ->
                    val parentTab = getComponent(tabItem.parentComponentId, pageEndPoint)
                    if (parentTab?.getComponentsItem()?.size.isMoreThanZero()) {
                        parentTab?.getComponentsItem()?.forEach { item ->
                            if(componentSync && item.id == preSelectedTab){
                                checkComponent(item)
                            }else if(!componentSync){
                                checkComponent(item)
                            }
                        }
                    }
                }
            }
        }
        return shouldReSync
    }

    private fun checkComponent(item: ComponentsItem) {
        if (item.getComponentsItem()?.size.isMoreThanZero()) {
            item.getComponentsItem()?.forEach { tabSubComps ->
                reInitialiseTabComponents(tabSubComps)
            }
        }
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
