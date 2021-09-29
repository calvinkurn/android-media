package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.merchantvoucher.MerchantVoucherRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import javax.inject.Inject

class MerchantVoucherUseCase @Inject constructor(private val repository: MerchantVoucherRepository) {
    companion object {
        private const val VOUCHER_PER_PAGE = 20
        private const val PAGE_START = 1
        private const val RPC_PAGE_NUMBER = "rpc_page_number"
        private const val RPC_NEXT_PAGE = "rpc_next_page"
        private const val RPC_PAGE_SIZE = "rpc_page_size"
    }
    suspend fun loadFirstPageComponents(componentId: String, pageEndPoint: String, productsLimit: Int = VOUCHER_PER_PAGE): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        if (component?.noOfPagesLoaded == 1) return false
        component?.let {
            val isDynamic = it.properties?.dynamic ?: false
            val (productListData,nextPage) = repository.getMerchantVouchers(
                if (isDynamic && !component.dynamicOriginalId.isNullOrEmpty())
                    component.dynamicOriginalId!! else componentId,
                getQueryParameterMap(
                    PAGE_START,
                    productsLimit,
                    it.nextPageKey,
                    it.userAddressData),
                pageEndPoint, it.name)
            it.showVerticalLoader = productListData.isNotEmpty()
            it.setComponentsItem(productListData, component.tabName)
            it.noOfPagesLoaded = 1
            it.nextPageKey = nextPage
            if (productListData.isEmpty()) return true
            it.pageLoadedCounter = 2
            it.verticalProductFailState = false
            return true
        }
        return false
    }

    private fun getQueryParameterMap(pageNumber: Int,
                                     productsPerPage: Int,
                                     nextPageKey : String?,
                                     userAddressData: LocalCacheModel?): MutableMap<String, Any> {

        val queryParameterMap = mutableMapOf<String, Any>()

        queryParameterMap[RPC_PAGE_SIZE] = productsPerPage.toString()
        queryParameterMap[RPC_PAGE_NUMBER] = pageNumber.toString()
        queryParameterMap[RPC_NEXT_PAGE] = nextPageKey ?: ""
        queryParameterMap.putAll(Utils.addAddressQueryMap(userAddressData))
        return queryParameterMap
    }
}