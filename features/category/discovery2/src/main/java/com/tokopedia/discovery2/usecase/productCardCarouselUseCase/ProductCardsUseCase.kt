package com.tokopedia.discovery2.usecase.productCardCarouselUseCase

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.productcards.ProductCardsRepository
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.CATEGORY_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.EMBED_CATEGORY
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.PIN_PRODUCT
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.PRODUCT_ID
import javax.inject.Inject

class ProductCardsUseCase @Inject constructor(private val productCardsRepository: ProductCardsRepository) {
    companion object {
        private const val RPC_ROWS = "rpc_Rows"
        private const val RPC_START = "rpc_Start"
        private const val PRODUCT_PER_PAGE = 20
        private const val RPC_FILTER_KEU = "rpc_"
        private const val PAGE_START = 0
    }

    suspend fun loadFirstPageComponents(componentId: String, pageEndPoint: String, queryMap: Map<String, String?>?): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        if (component?.noOfPagesLoaded == 1)
            return false
        component?.let {
            val parentComponentsItem = getComponent(it.parentComponentId, pageEndPoint)
            val isDynamic = it.properties?.dynamic ?: false
            val productsList = productCardsRepository.getProducts(
                    if (isDynamic && !component.dynamicOriginalId.isNullOrEmpty())
                        component.dynamicOriginalId!! else componentId,
                    getQueryParameterMap(PAGE_START,
                            it.properties,
                            parentComponentsItem?.chipSelectionData,
                            it.selectedFilters,
                            it.selectedSort,
                            queryMap,
                            parentComponentsItem?.data),
                    pageEndPoint, it.name)

            it.showVerticalLoader = productsList.isNotEmpty()
            it.setComponentsItem(productsList)
            it.noOfPagesLoaded = 1
            return true
        }
        return false
    }

    suspend fun getProductCardsUseCase(componentId: String, pageEndPoint: String, rpcDiscoQuery: Map<String, String?>?): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        val parentComponent = component?.parentComponentId?.let { getComponent(it, pageEndPoint) }

        parentComponent?.let { component1 ->
            val isDynamic = component1.properties?.dynamic ?: false
            val parentComponentsItem = getComponent(component1.parentComponentId, pageEndPoint)
            val size = component1.getComponentsItem()?.size
            val productsList = productCardsRepository.getProducts(
                    if (isDynamic && !component1.dynamicOriginalId.isNullOrEmpty())
                        component1.dynamicOriginalId!! else component1.id,
                    getQueryParameterMap(size ?: 0,
                            component1.properties,
                            parentComponentsItem?.chipSelectionData,
                            component1.selectedFilters,
                            component1.selectedSort,
                            rpcDiscoQuery,
                            parentComponentsItem?.data),
                    pageEndPoint,
                    component1.name)
            if (productsList.isEmpty()) {
                component1.showVerticalLoader = false
            } else {
                component1.showVerticalLoader = true
                (component1.getComponentsItem() as ArrayList<ComponentsItem>).addAll(productsList)
            }
            return true
        }
        return false
    }

    private fun getQueryParameterMap(pageStart: Int,
                                     properties: Properties?,
                                     chipSelectionData: DataItem?,
                                     selectedFilters: HashMap<String, String>?,
                                     selectedSort: HashMap<String, String>?,
                                     queryMap: Map<String, String?>? = null,
                                     data: List<DataItem>?): MutableMap<String, Any> {
        val productsPerPage: String = properties?.run {
            limitNumber.takeIf { limitProduct }
        } ?: PRODUCT_PER_PAGE.toString()
        val queryParameterMap = mutableMapOf<String, Any>()
        queryParameterMap[RPC_ROWS] = productsPerPage
        queryParameterMap[RPC_START] = pageStart.toString()
        chipSelectionData?.let {
            queryParameterMap[RPC_FILTER_KEU + it.key] = it.value.toString()
        }
        selectedFilters?.let {
            for (map in it) {
                queryParameterMap[RPC_FILTER_KEU + map.key] = map.value
            }
        }
        selectedSort?.let {
            for (map in it) {
                queryParameterMap[RPC_FILTER_KEU + map.key] = map.value
            }
        }

        queryMap?.let {
            if (!it[PIN_PRODUCT].isNullOrEmpty()) {
                queryParameterMap[PIN_PRODUCT] = it[PIN_PRODUCT] ?: ""
                queryParameterMap[PRODUCT_ID] = it[PRODUCT_ID] ?: ""
            } else if (!it[EMBED_CATEGORY].isNullOrEmpty()) {
                queryParameterMap[EMBED_CATEGORY] = it[EMBED_CATEGORY] ?: ""
                queryParameterMap[CATEGORY_ID] = it[CATEGORY_ID] ?: ""
            }
        }

        if (!data.isNullOrEmpty()) {
            val item = data[0]
            if (!item.filterKey.isNullOrEmpty() && !item.filterValue.isNullOrEmpty()) {
                queryParameterMap[RPC_FILTER_KEU + item.filterKey] = item.filterValue
            }
        }
        return queryParameterMap
    }
}