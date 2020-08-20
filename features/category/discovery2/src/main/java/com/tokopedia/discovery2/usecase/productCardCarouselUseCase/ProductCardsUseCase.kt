package com.tokopedia.discovery2.usecase.productCardCarouselUseCase

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.productcards.ProductCardsRepository
import java.util.HashMap
import javax.inject.Inject

class ProductCardsUseCase @Inject constructor(private val productCardsRepository: ProductCardsRepository) {
    companion object {
        private const val RPC_ROWS = "rpc_Rows"
        private const val RPC_START = "rpc_Start"
        private const val PRODUCT_PER_PAGE = 20
        private const val RPC_FILTER_KEU = "rpc_"
    }

    suspend fun loadFirstPageComponents(componentId: String, pageEndPoint: String): Boolean {
        var component = getComponent(componentId, pageEndPoint)
        if (component?.noOfPagesLoaded == 1)
            return false
        component?.let { component ->
            val parentComponentsItem = getComponent(component.parentComponentId, pageEndPoint)
            component?.setComponentsItem(productCardsRepository.getProducts(componentId, getQueryParameterMap(0, component.componentsPerPage, parentComponentsItem?.chipSelectionData, component.selectedFilters, component.selectedSort), pageEndPoint, component.name))
            component?.noOfPagesLoaded = 1
            return true
        }

        return false
    }

    suspend fun getProductCardsUseCase(componentId: String, pageEndPoint: String): Boolean {
        var component = getComponent(componentId, pageEndPoint)
        var parentComponent = component?.parentComponentId?.let { getComponent(it, pageEndPoint) }

        parentComponent?.let { component1 ->
            val parentComponentsItem = getComponent(component1.parentComponentId, pageEndPoint)
            var size = component1.getComponentsItem()?.size
            (component1?.getComponentsItem() as ArrayList<ComponentsItem>).addAll(productCardsRepository.getProducts(component1.id, getQueryParameterMap(size
                    ?: 0, component1.componentsPerPage, parentComponentsItem?.chipSelectionData, component1.selectedFilters, component1.selectedSort), pageEndPoint, component1.name))
            return true
        }
        return false
    }

    private fun getQueryParameterMap(pageStart: Int, productPerPage: Int?, chipSelectionData: DataItem?, selectedFilters : HashMap<String, String>?, selectedSort : HashMap<String, String>?): MutableMap<String, Any> {
        val queryParameterMap = mutableMapOf<String, Any>()
        queryParameterMap[RPC_ROWS] = PRODUCT_PER_PAGE.toString()
        queryParameterMap[RPC_START] = pageStart.toString()
        chipSelectionData?.let {
            queryParameterMap[RPC_FILTER_KEU + it.key] = it.value.toString()
        }
        selectedFilters?.let {
            for(map in it){
                queryParameterMap[RPC_FILTER_KEU + map.key] = map.value
            }
        }
        selectedSort?.let {
            for(map in it){
                queryParameterMap[RPC_FILTER_KEU + map.key] = map.value
            }
        }
        return queryParameterMap
    }
}