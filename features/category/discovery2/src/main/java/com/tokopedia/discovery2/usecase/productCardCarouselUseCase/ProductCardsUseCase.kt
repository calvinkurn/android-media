package com.tokopedia.discovery2.usecase.productCardCarouselUseCase

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.productcards.ProductCardsRepository
import java.util.*
import javax.inject.Inject

class ProductCardsUseCase @Inject constructor(private val productCardsRepository: ProductCardsRepository) {
    companion object {
        private const val RPC_ROWS = "rpc_Rows"
        private const val RPC_START = "rpc_Start"
        private const val PRODUCT_PER_PAGE = 20
        private const val RPC_FILTER_KEU = "rpc_"
        private const val rpc_PINNED_PRODUCT = "rpc_PinnedProduct"
    }

    suspend fun loadFirstPageComponents(componentId: String, pageEndPoint: String, rpcPinnedProduct: String?): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        if (component?.noOfPagesLoaded == 1)
            return false
        component?.let { item ->
            val parentComponentsItem = getComponent(item.parentComponentId, pageEndPoint)
            item.setComponentsItem(productCardsRepository.getProducts(componentId, getQueryParameterMap(0, item.properties, parentComponentsItem?.chipSelectionData, item.selectedFilters, item.selectedSort, rpcPinnedProduct), pageEndPoint, item.name))
            item.noOfPagesLoaded = 1
            return true
        }

        return false
    }

    suspend fun getProductCardsUseCase(componentId: String, pageEndPoint: String): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        val parentComponent = component?.parentComponentId?.let { getComponent(it, pageEndPoint) }

        parentComponent?.let { component1 ->
            val parentComponentsItem = getComponent(component1.parentComponentId, pageEndPoint)
            val size = component1.getComponentsItem()?.size
            (component1.getComponentsItem() as ArrayList<ComponentsItem>).addAll(productCardsRepository.getProducts(component1.id, getQueryParameterMap(size
                    ?: 0, component1.properties, parentComponentsItem?.chipSelectionData, component1.selectedFilters, component1.selectedSort), pageEndPoint, component1.name))
            return true
        }
        return false
    }

    private fun getQueryParameterMap(pageStart: Int, properties: Properties?, chipSelectionData: DataItem?, selectedFilters: HashMap<String, String>?, selectedSort: HashMap<String, String>?, rpcPinnedProduct: String? = null): MutableMap<String, Any> {
        val productsPerPage:String = if(properties!=null && properties.limitProduct){
            properties.limitNumber
        }else{
            PRODUCT_PER_PAGE.toString()
        }
        val queryParameterMap = mutableMapOf<String, Any>()
        queryParameterMap[RPC_ROWS] = productsPerPage
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
        if (!rpcPinnedProduct.isNullOrEmpty()) {
            queryParameterMap[rpc_PINNED_PRODUCT] = rpcPinnedProduct
        }
        return queryParameterMap
    }
}