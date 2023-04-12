package com.tokopedia.discovery2.usecase.productbundlingusecase

import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.datamapper.getMapWithoutRpc
import com.tokopedia.discovery2.repository.productbundling.ProductBundlingRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import javax.inject.Inject

class ProductBundlingUseCase @Inject constructor(private val productBundlingRepository: ProductBundlingRepository) {
    companion object {
        private const val BUNDLING_PER_PAGE = 10
        private const val PAGE_START = 1
        private const val PAGE_LOADED_2 = 2
    }

    suspend fun loadFirstPageComponents(componentId: String, pageEndPoint: String, bundlingPageSize:Int = BUNDLING_PER_PAGE): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        val paramWithoutRpc = getMapWithoutRpc(pageEndPoint)
        if (component?.noOfPagesLoaded == 1) return false
        component?.let {
            val isDynamic = it.properties?.dynamic ?: false
            val (productListData, nextPage) = productBundlingRepository.getProductBundlingData(
                if (isDynamic && !component.dynamicOriginalId.isNullOrEmpty())
                    component.dynamicOriginalId!! else componentId,
                getQueryParameterMap(PAGE_START,
                    bundlingPageSize,
                    it.nextPageKey,
                    paramWithoutRpc,
                    it.userAddressData,
                    it.selectedFilters,
                    it.selectedSort
                ),
                pageEndPoint, it.name)
            it.showVerticalLoader = !productListData.isNullOrEmpty()
            it.noOfPagesLoaded = PAGE_START
            it.nextPageKey = nextPage
            if (productListData.isNullOrEmpty()) return true
            it.pageLoadedCounter = PAGE_LOADED_2
            it.verticalProductFailState = false
            it.noOfPagesLoaded = 1
            it.data = productListData
            return true
        }
        return false
    }

    suspend fun getProductBundlingPaginatedData(componentId: String, pageEndPoint: String, bundlingPageSize: Int = BUNDLING_PER_PAGE): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        val paramWithoutRpc = getMapWithoutRpc(pageEndPoint)
        component?.let {
            val isDynamic = it.properties?.dynamic ?: false
            val (productListData, nextPage) = productBundlingRepository.getProductBundlingData(
                if (isDynamic && !component.dynamicOriginalId.isNullOrEmpty()) component.dynamicOriginalId!! else componentId,
                getQueryParameterMap(it.pageLoadedCounter,
                    bundlingPageSize,
                    it.nextPageKey,
                    paramWithoutRpc,
                    it.userAddressData,
                    it.selectedFilters,
                    it.selectedSort),
                pageEndPoint,
                it.name)
            component.nextPageKey = nextPage
            if (productListData.isNullOrEmpty()){
                return false
            } else {
                it.pageLoadedCounter += 1
            }

            (it.data as ArrayList<DataItem>).addAll(productListData)
            return true
        }
        return false
    }


    private fun getQueryParameterMap(pageNumber: Int,
                                     bundlingPerPage: Int,
                                     nextPageKey: String?,
                                     queryParameterMapWithoutRpc: Map<String, String>?,
                                     userAddressData: LocalCacheModel?,
                                     selectedFilters: HashMap<String, String>?,
                                     selectedSort: HashMap<String, String>?): MutableMap<String, Any> {

        val queryParameterMap = mutableMapOf<String, Any>()

        queryParameterMap[Utils.RPC_PAGE_SIZE] = bundlingPerPage.toString()
        queryParameterMap[Utils.RPC_PAGE_NUMBER] = pageNumber.toString()
        queryParameterMap[Utils.RPC_NEXT_PAGE] = nextPageKey ?: ""
        queryParameterMapWithoutRpc?.let {
            queryParameterMap.putAll(it)
        }

        selectedFilters?.let {
            for (map in it) {
                queryParameterMap[Utils.RPC_FILTER_KEY + map.key] = map.value
            }
        }
        selectedSort?.let {
            for (map in it) {
                queryParameterMap[Utils.RPC_FILTER_KEY + map.key] = map.value
            }
        }

        queryParameterMap.putAll(Utils.addAddressQueryMapWithWareHouse(userAddressData))

        return queryParameterMap
    }
}
