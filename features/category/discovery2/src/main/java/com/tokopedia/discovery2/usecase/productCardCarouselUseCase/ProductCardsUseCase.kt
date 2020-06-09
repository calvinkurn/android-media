package com.tokopedia.discovery2.usecase.productCardCarouselUseCase

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.productcards.ProductCardsRepository
import javax.inject.Inject

class ProductCardsUseCase @Inject constructor(private val productCardsRepository: ProductCardsRepository) {
    companion object {
        private const val RPC_PAGE_NUMBER_KEY = "rpc_page_number"
        private const val RPC_PAGE_SIZE = "rpc_page_size"
        private const val RPC_FILTER_KEU = "rpc_"
    }

    suspend fun loadFirstPageComponents(componentId: String, pageEndPoint: String): Boolean {
        var component = getComponent(componentId, pageEndPoint)
        if (component?.noOfPagesLoaded == 1)
            return false
        component?.let { component ->
            val parentComponentsItem = getComponent(component.parentComponentId,pageEndPoint)
            component?.setComponentsItem(productCardsRepository.getProducts(componentId, getQueryParameterMap(1, component.componentsPerPage,parentComponentsItem?.chipSelectionData), pageEndPoint, component.name))
            component?.noOfPagesLoaded = 1
            return true
        }

        return false
    }

    suspend fun getProductCardsUseCase(componentId: String, queryParameterMap: MutableMap<String, Any>, pageEndPoint: String, productComponentName: String?): ArrayList<ComponentsItem> {
        return productCardsRepository.getProducts(componentId, queryParameterMap, pageEndPoint, productComponentName)
    }

    private fun getQueryParameterMap(pageNum: Int, productPerPage: Int?,chipSelectionData:DataItem?): MutableMap<String, Any> {
        val queryParameterMap = mutableMapOf<String, Any>()
        queryParameterMap[RPC_PAGE_NUMBER_KEY] = pageNum.toString()
        queryParameterMap[RPC_PAGE_SIZE] = productPerPage.toString()
        chipSelectionData?.let {
            queryParameterMap[RPC_FILTER_KEU+it.key] = it.value.toString()
        }
        return queryParameterMap
    }
}