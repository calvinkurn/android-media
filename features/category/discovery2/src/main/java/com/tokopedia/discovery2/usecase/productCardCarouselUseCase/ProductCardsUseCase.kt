package com.tokopedia.discovery2.usecase.productCardCarouselUseCase

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.productcards.ProductCardsRepository
import javax.inject.Inject

class ProductCardsUseCase @Inject constructor(private val productCardsRepository: ProductCardsRepository) {
    companion object {
        private const val RPC_ROWS = "rpc_Rows"
        private const val RPC_START = "rpc_Start"
        private const val PRODUCT_PER_PAGE = 20
    }

    suspend fun loadFirstPageComponents(componentId: String, pageEndPoint: String): Boolean {
        var component = getComponent(componentId, pageEndPoint)
        if (component?.noOfPagesLoaded == 1)
            return false
        component?.let { component ->
            component?.componentsItem = productCardsRepository.getProducts(componentId, getQueryParameterMap(0, component.componentsPerPage), pageEndPoint, component.name)
            component?.noOfPagesLoaded = 1
            return true
        }

        return false
    }

    suspend fun getProductCardsUseCase(componentId: String, pageEndPoint: String): Boolean {
        var component = getComponent(componentId, pageEndPoint)
        var parentComponent = component?.parentComponentId?.let { getComponent(it, pageEndPoint) }

        parentComponent?.let { component ->
            var size = component.componentsItem?.size
            (component?.componentsItem as ArrayList<ComponentsItem>).addAll(productCardsRepository.getProducts(componentId, getQueryParameterMap(size
                    ?: 0, component.componentsPerPage), pageEndPoint, component.name))
            return true
        }
        return false
    }

    private fun getQueryParameterMap(pageStart: Int, productPerPage: Int?): MutableMap<String, Any> {
        val queryParameterMap = mutableMapOf<String, Any>()
        queryParameterMap[RPC_ROWS] = PRODUCT_PER_PAGE.toString()
        queryParameterMap[RPC_START] = pageStart.toString()
        return queryParameterMap
    }
}