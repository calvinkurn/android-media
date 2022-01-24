package com.tokopedia.discovery2.usecase.bannerusecase

import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.banner.BannerRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import javax.inject.Inject

class BannerUseCase @Inject constructor(private val repository: BannerRepository) {
    companion object {
        private const val BANNER_PER_PAGE = 10
        private const val PAGE_START = 1
        private const val RPC_PAGE_NUMBER = "rpc_page_number"
        private const val RPC_NEXT_PAGE = "rpc_next_page"
        private const val RPC_PAGE_SIZE = "rpc_page_size"
    }

    suspend fun loadFirstPageComponents(componentId: String, pageEndPoint: String, productsLimit: Int = BANNER_PER_PAGE): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        if (component?.noOfPagesLoaded == 1) return false
        component?.let {
            val isDynamic = it.properties?.dynamic ?: false
            val voucherListData = repository.getBanner(
                    if (isDynamic && !component.dynamicOriginalId.isNullOrEmpty())
                        component.dynamicOriginalId!! else componentId,
                    getQueryParameterMap(
                            PAGE_START,
                            productsLimit,
                            it.nextPageKey,
                            it.userAddressData),
                    pageEndPoint, it.name)
//            it.showVerticalLoader = voucherListData.isNotEmpty()
//            it.setComponentsItem(it.getComponentsItem(), component.tabName)
            it.noOfPagesLoaded = 1
            if (voucherListData.isEmpty()) return true

            it.data = voucherListData
//            it.pageLoadedCounter = 2
//            it.verticalProductFailState = false
            return true
        }
        return false
    }

    private fun getQueryParameterMap(pageNumber: Int,
                                     productsPerPage: Int,
                                     nextPageKey: String?,
                                     userAddressData: LocalCacheModel?): MutableMap<String, Any> {

        val queryParameterMap = mutableMapOf<String, Any>()

        queryParameterMap[RPC_PAGE_SIZE] = productsPerPage.toString()
        queryParameterMap[RPC_PAGE_NUMBER] = pageNumber.toString()
        queryParameterMap[RPC_NEXT_PAGE] = nextPageKey ?: ""
        queryParameterMap.putAll(Utils.addAddressQueryMap(userAddressData))
        return queryParameterMap
    }
}