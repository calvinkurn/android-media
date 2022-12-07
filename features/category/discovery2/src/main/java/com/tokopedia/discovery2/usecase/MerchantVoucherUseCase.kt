package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.datamapper.getMapWithoutRpc
import com.tokopedia.discovery2.repository.merchantvoucher.MerchantVoucherRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import javax.inject.Inject

class MerchantVoucherUseCase @Inject constructor(private val repository: MerchantVoucherRepository) {
    companion object {
        private const val VOUCHER_PER_PAGE = 10
        private const val PAGE_START = 1
    }
    suspend fun loadFirstPageComponents(componentId: String, pageEndPoint: String, productsLimit: Int = VOUCHER_PER_PAGE): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        val paramWithoutRpc = getMapWithoutRpc(pageEndPoint)
        if (component?.noOfPagesLoaded == 1) return false
        component?.let {
            val isDynamic = it.properties?.dynamic ?: false
            val (voucherListData,nextPage) = repository.getMerchantVouchers(
                if (isDynamic && !component.dynamicOriginalId.isNullOrEmpty())
                    component.dynamicOriginalId!! else componentId,
                getQueryParameterMap(
                    PAGE_START,
                    productsLimit,
                    it.nextPageKey,
                    it.userAddressData,
                    it.selectedFilters,
                    it.selectedSort,
                    paramWithoutRpc
                ),
                pageEndPoint,
                it.name
            )
            it.showVerticalLoader = voucherListData.isNotEmpty()
            it.setComponentsItem(voucherListData, component.tabName)
            it.noOfPagesLoaded = 1
            it.nextPageKey = nextPage
            if (voucherListData.isEmpty()) return true
            it.pageLoadedCounter = 2
            it.verticalProductFailState = false
            return true
        }
        return false
    }

    suspend fun getVoucherUseCase(componentId: String, pageEndPoint: String, productsLimit: Int = VOUCHER_PER_PAGE): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        val paramWithoutRpc = getMapWithoutRpc(pageEndPoint)
        val parentComponent = component?.parentComponentId?.let { getComponent(it, pageEndPoint) }
        parentComponent?.let { component1 ->
            val isDynamic = component1.properties?.dynamic ?: false
            val (voucherListData,nextPage) = repository.getMerchantVouchers(
                if (isDynamic && !component1.dynamicOriginalId.isNullOrEmpty())
                    component1.dynamicOriginalId!! else component1.id,
                getQueryParameterMap(component1.pageLoadedCounter,
                    productsLimit,
                    component1.nextPageKey,
                    component1.userAddressData,
                    component1.selectedFilters,
                    component1.selectedSort,
                    paramWithoutRpc
                ),
                pageEndPoint,
                component1.name)
            component1.nextPageKey = nextPage
            if (voucherListData.isEmpty()) {
                component1.showVerticalLoader = false
            } else {
                component1.pageLoadedCounter += 1
                component1.showVerticalLoader = true
                updatePaginatedData(voucherListData,component1)
                (component1.getComponentsItem() as ArrayList<ComponentsItem>).addAll(voucherListData)
            }
            component1.verticalProductFailState = false
            return true
        }
        return false
    }

    suspend fun getPaginatedData(componentId: String, pageEndPoint: String, productsLimit: Int = VOUCHER_PER_PAGE): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        val paramWithoutRpc = getMapWithoutRpc(pageEndPoint)
        val parentComponent = component?.parentComponentId?.let { getComponent(it, pageEndPoint) }
        parentComponent?.let { component1 ->
            val isDynamic = component1.properties?.dynamic ?: false
            val (voucherListData,nextPage) = repository.getMerchantVouchers(
                if (isDynamic && !component1.dynamicOriginalId.isNullOrEmpty())
                    component1.dynamicOriginalId!! else component1.id,
                getQueryParameterMap(component1.pageLoadedCounter,
                    productsLimit,
                    component1.nextPageKey,
                    component1.userAddressData,
                    component1.selectedFilters,
                    component1.selectedSort,
                    paramWithoutRpc
                ),
                pageEndPoint,
                component1.name)
            component1.nextPageKey = nextPage
            if (voucherListData.isEmpty()) {
                component1.showVerticalLoader = false
            } else {
                component1.pageLoadedCounter += 1
                component1.showVerticalLoader = true
                updatePaginatedData(voucherListData,component1)
                (component1.getComponentsItem() as ArrayList<ComponentsItem>).addAll(voucherListData)
            }
            component1.verticalProductFailState = false
            return true
        }
        return false
    }

    suspend fun getCarouselPaginatedData(componentId: String, pageEndPoint: String, productsLimit: Int = VOUCHER_PER_PAGE): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        val paramWithoutRpc = getMapWithoutRpc(pageEndPoint)
        component?.let {
            val isDynamic = it.properties?.dynamic ?: false
            val (voucherListData,nextPage) = repository.getMerchantVouchers(
                if (isDynamic && !component.dynamicOriginalId.isNullOrEmpty()) component.dynamicOriginalId!! else componentId,
                getQueryParameterMap(component.pageLoadedCounter,
                    productsLimit,
                    component.nextPageKey,
                    component.userAddressData,
                    component.selectedFilters,
                    component.selectedSort,
                    paramWithoutRpc
                ),
                pageEndPoint,
                it.name
            )
            component.nextPageKey = nextPage
            if (voucherListData.isEmpty()) return false else it.pageLoadedCounter += 1
            updatePaginatedData(voucherListData,it)
            (it.getComponentsItem() as ArrayList<ComponentsItem>).addAll(voucherListData)
            return true
        }
        return false
    }

    private fun updatePaginatedData(voucherListData:ArrayList<ComponentsItem>,parentComponentsItem: ComponentsItem){
        voucherListData.forEach {
            it.parentComponentId = parentComponentsItem.id
            it.pageEndPoint = parentComponentsItem.pageEndPoint
            it.parentComponentPosition = parentComponentsItem.position
        }
    }

    private fun getQueryParameterMap(
        pageNumber: Int,
        productsPerPage: Int,
        nextPageKey: String?,
        userAddressData: LocalCacheModel?,
        selectedFilters: HashMap<String, String>?,
        selectedSort: HashMap<String, String>?,
        queryParameterMapWithoutRpc: Map<String, String>?
    ): MutableMap<String, Any> {
        val queryParameterMap = mutableMapOf<String, Any>()

        queryParameterMap[Utils.RPC_PAGE_SIZE] = productsPerPage.toString()
        queryParameterMap[Utils.RPC_PAGE_NUMBER] = pageNumber.toString()
        queryParameterMap[Utils.RPC_NEXT_PAGE] = nextPageKey ?: ""
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
        queryParameterMapWithoutRpc?.let {
            queryParameterMap.putAll(it)
        }
        queryParameterMap.putAll(Utils.addAddressQueryMap(userAddressData))
        return queryParameterMap
    }
}
