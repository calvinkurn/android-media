package com.tokopedia.discovery2.usecase.productCardCarouselUseCase

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Utils.Companion.addAddressQueryMap
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.discoComponentQuery
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.productcards.ProductCardsRepository
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.CATEGORY_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.EMBED_CATEGORY
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.PIN_PRODUCT
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.PRODUCT_ID
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import javax.inject.Inject

class ProductCardsUseCase @Inject constructor(private val productCardsRepository: ProductCardsRepository) {
    companion object {
        private const val PRODUCT_PER_PAGE = 20
        private const val RPC_FILTER_KEU = "rpc_"
        private const val PAGE_START = 1
        private const val RPC_PAGE_NUMBER = "rpc_page_number"
        private const val RPC_PAGE__SIZE = "rpc_page_size"
    }

    suspend fun loadFirstPageComponents(componentId: String, pageEndPoint: String, productsLimit: Int = PRODUCT_PER_PAGE): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        if (component?.noOfPagesLoaded == 1) return false
        component?.let {
            val parentComponentsItem = getComponent(it.parentComponentId, pageEndPoint)
            val isDynamic = it.properties?.dynamic ?: false
            val productListData = productCardsRepository.getProducts(
                    if (isDynamic && !component.dynamicOriginalId.isNullOrEmpty())
                        component.dynamicOriginalId!! else componentId,
                    getQueryParameterMap(PAGE_START,
                            parentComponentsItem?.chipSelectionData,
                            it.selectedFilters,
                            it.selectedSort,
                            parentComponentsItem?.data,
                            productsLimit,
                            componentId,
                            pageEndPoint,
                            it.userAddressData),
                    pageEndPoint, it.name)
            it.showVerticalLoader = productListData.isNotEmpty()
            it.setComponentsItem(productListData, component.tabName)
            it.noOfPagesLoaded = 1
            if (productListData.isEmpty()) return true
            it.pageLoadedCounter = 2
            it.productListFailState = false
            return true
        }
        return false
    }

    suspend fun getProductCardsUseCase(componentId: String, pageEndPoint: String, productsLimit: Int = PRODUCT_PER_PAGE): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        val parentComponent = component?.parentComponentId?.let { getComponent(it, pageEndPoint) }
        parentComponent?.let { component1 ->
            val isDynamic = component1.properties?.dynamic ?: false
            val parentComponentsItem = getComponent(component1.parentComponentId, pageEndPoint)
            val productListData = productCardsRepository.getProducts(
                    if (isDynamic && !component1.dynamicOriginalId.isNullOrEmpty())
                        component1.dynamicOriginalId!! else component1.id,
                    getQueryParameterMap(component1.pageLoadedCounter,
                            parentComponentsItem?.chipSelectionData,
                            component1.selectedFilters,
                            component1.selectedSort,
                            parentComponentsItem?.data,
                            productsLimit,
                            componentId,
                            pageEndPoint,
                            component.userAddressData),
                    pageEndPoint,
                    component1.name)

            if (productListData.isEmpty()) {
                component1.showVerticalLoader = false
            } else {
                component1.pageLoadedCounter += 1
                component1.showVerticalLoader = true
                (component1.getComponentsItem() as ArrayList<ComponentsItem>).addAll(productListData)
            }
            component1.productListFailState = false
            return true
        }
        return false
    }

    suspend fun getCarouselPaginatedData(componentId: String, pageEndPoint: String, productsLimit: Int = PRODUCT_PER_PAGE): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        component?.let {
            it.properties?.let { properties ->
                if (properties.limitProduct && properties.limitNumber.toIntOrZero() == it.getComponentsItem()?.size) return false
            }
            val parentComponentsItem = getComponent(it.parentComponentId, pageEndPoint)
            val isDynamic = it.properties?.dynamic ?: false
            val productListData = productCardsRepository.getProducts(
                    if (isDynamic && !component.dynamicOriginalId.isNullOrEmpty()) component.dynamicOriginalId!! else componentId,
                    getQueryParameterMap(it.pageLoadedCounter,
                            parentComponentsItem?.chipSelectionData,
                            it.selectedFilters,
                            it.selectedSort,
                            parentComponentsItem?.data,
                            productsLimit,
                            componentId,
                            pageEndPoint,
                            it.userAddressData),
                    pageEndPoint,
                    it.name)
            if (productListData.isEmpty()) return false else it.pageLoadedCounter += 1
            (it.getComponentsItem() as ArrayList<ComponentsItem>).addAll(productListData)
            return true
        }
        return false
    }

    private fun getQueryParameterMap(pageNumber: Int,
                                     chipSelectionData: DataItem?,
                                     selectedFilters: HashMap<String, String>?,
                                     selectedSort: HashMap<String, String>?,
                                     data: List<DataItem>?,
                                     productsPerPage: Int,
                                     componentId: String,
                                     pageEndPoint: String,
                                     userAddressData: LocalCacheModel?): MutableMap<String, Any> {

        val queryParameterMap = mutableMapOf<String, Any>()

        queryParameterMap[RPC_PAGE__SIZE] = productsPerPage.toString()
        queryParameterMap[RPC_PAGE_NUMBER] = pageNumber.toString()

        chipSelectionData?.let {
            it.targetComponent?.split(",")?.forEach { targetId ->
                if (componentId == ComponentNames.LoadMore.componentName) {
                    val component = getComponent(componentId, pageEndPoint)
                    if (targetId == component?.parentComponentId) {
                        queryParameterMap[RPC_FILTER_KEU + it.key] = it.value.toString()
                    }
                } else if (targetId == componentId) queryParameterMap[RPC_FILTER_KEU + it.key] = it.value.toString()
            }
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

        discoComponentQuery?.let {
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

        queryParameterMap.putAll(addAddressQueryMap(userAddressData))
        return queryParameterMap
    }
}