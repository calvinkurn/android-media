package com.tokopedia.discovery2.usecase.shopcardusecase

import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.Utils.Companion.RPC_NEXT_PAGE
import com.tokopedia.discovery2.Utils.Companion.RPC_PAGE_NUMBER
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.shopcard.ShopCardRepository
import javax.inject.Inject

class ShopCardUseCase @Inject constructor(private val shopCardRepository: ShopCardRepository) {
    companion object {
        private const val SHOP_PER_PAGE = 10
        private const val PAGE_START = 1
        private const val PAGE_LOADED_2 = 1
    }

    suspend fun loadFirstPageComponents(componentId: String, pageEndPoint: String, shopLimit: Int = SHOP_PER_PAGE): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        if (component?.noOfPagesLoaded == 1) return false
        component?.let {
            val isDynamic = it.properties?.dynamic ?: false
            val (shopCardListData, nextPage) = shopCardRepository.getShopCardData(
                    if (isDynamic && !component.dynamicOriginalId.isNullOrEmpty())
                        component.dynamicOriginalId!! else componentId,
                    getQueryParameterMap(PAGE_START,
                            shopLimit,
                            it.nextPageKey),
                    pageEndPoint, it.name)
            it.showVerticalLoader = shopCardListData.isNotEmpty()
            it.setComponentsItem(shopCardListData, component.tabName)
            it.noOfPagesLoaded = PAGE_START
            it.nextPageKey = nextPage
            if (shopCardListData.isEmpty()) return true
            it.pageLoadedCounter = PAGE_LOADED_2
            it.verticalProductFailState = false
            return true
        }
        return false
    }

    suspend fun getShopCardPaginatedData(componentId: String, pageEndPoint: String, shopLimit: Int = SHOP_PER_PAGE): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        component?.let {
            val isDynamic = it.properties?.dynamic ?: false
            val (shopCardListData, nextPage) = shopCardRepository.getShopCardData(
                    if (isDynamic && !component.dynamicOriginalId.isNullOrEmpty()) component.dynamicOriginalId!! else componentId,
                    getQueryParameterMap(it.pageLoadedCounter,
                            shopLimit,
                            it.nextPageKey),
                    pageEndPoint,
                    it.name)
            component.nextPageKey = nextPage
            if (shopCardListData.isEmpty()) return false else it.pageLoadedCounter += 1

            (it.getComponentsItem() as ArrayList<ComponentsItem>).addAll(shopCardListData)
            return true
        }
        return false
    }

    private fun getQueryParameterMap(pageNumber: Int,
                                     shopPerPage: Int,
                                     nextPageKey: String?): MutableMap<String, Any> {

        val queryParameterMap = mutableMapOf<String, Any>()

        queryParameterMap[Utils.RPC_PAGE__SIZE] = shopPerPage.toString()
        queryParameterMap[RPC_PAGE_NUMBER] = pageNumber.toString()

        queryParameterMap[RPC_NEXT_PAGE] = nextPageKey ?: ""

        return queryParameterMap
    }
}