package com.tokopedia.discovery2.usecase.shopcardusecase

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.discoComponentQuery
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.shopcard.ShopCardRepository
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.CATEGORY_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.EMBED_CATEGORY
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.PIN_PRODUCT
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.PRODUCT_ID
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import javax.inject.Inject

class ShopCardUseCase @Inject constructor(private val shopCardRepository: ShopCardRepository) {
    companion object {
        private const val SHOP_PER_PAGE = 20
        private const val PAGE_START = 1
        private const val RPC_PAGE_NUMBER = "rpc_page_number"
        private const val RPC_NEXT_PAGE = "rpc_next_page"
        private const val RPC_PAGE__SIZE = "rpc_page_size"
    }

    suspend fun loadFirstPageComponents(componentId: String, pageEndPoint: String, shopLimit: Int = SHOP_PER_PAGE): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        if (component?.noOfPagesLoaded == 1) return false
        component?.let {
            val parentComponentsItem = getComponent(it.parentComponentId, pageEndPoint)
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
            it.noOfPagesLoaded = 1
            it.nextPageKey = nextPage
            if (shopCardListData.isEmpty()) return true
            it.pageLoadedCounter = 2
            it.verticalProductFailState = false
            return true
        }
        return false
    }

    suspend fun getProductCardsUseCase(componentId: String, pageEndPoint: String, shopLimit: Int = SHOP_PER_PAGE): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        val parentComponent = component?.parentComponentId?.let { getComponent(it, pageEndPoint) }
        parentComponent?.let { component1 ->
            val isDynamic = component1.properties?.dynamic ?: false
            val parentComponentsItem = getComponent(component1.parentComponentId, pageEndPoint)
            val (shopCardListData, nextPage) = shopCardRepository.getShopCardData(
                    if (isDynamic && !component1.dynamicOriginalId.isNullOrEmpty())
                        component1.dynamicOriginalId!! else component1.id,
                    getQueryParameterMap(component1.pageLoadedCounter,
                            shopLimit,
                            component1.nextPageKey),
                    pageEndPoint,
                    component1.name)
            component1.nextPageKey = nextPage
            if (shopCardListData.isEmpty()) {
                component1.showVerticalLoader = false
            } else {
                component1.pageLoadedCounter += 1
                component1.showVerticalLoader = true
                (component1.getComponentsItem() as ArrayList<ComponentsItem>).addAll(shopCardListData)
            }
            component1.verticalProductFailState = false
            return true
        }
        return false
    }

    suspend fun getShopCardPaginatedData(componentId: String, pageEndPoint: String, shopLimit: Int = SHOP_PER_PAGE): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        component?.let {
            it.properties?.let { properties ->
                if (properties.limitProduct && properties.limitNumber.toIntOrZero() == it.getComponentsItem()?.size) return false
            }
            val parentComponentsItem = getComponent(it.parentComponentId, pageEndPoint)
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

        queryParameterMap[RPC_PAGE__SIZE] = shopPerPage.toString()
        queryParameterMap[RPC_PAGE_NUMBER] = pageNumber.toString()

        discoComponentQuery?.let {
            if (!it[PIN_PRODUCT].isNullOrEmpty()) {
                queryParameterMap[PIN_PRODUCT] = it[PIN_PRODUCT] ?: ""
                queryParameterMap[PRODUCT_ID] = it[PRODUCT_ID] ?: ""
            } else if (!it[EMBED_CATEGORY].isNullOrEmpty()) {
                queryParameterMap[EMBED_CATEGORY] = it[EMBED_CATEGORY] ?: ""
                queryParameterMap[CATEGORY_ID] = it[CATEGORY_ID] ?: ""
            }
        }

        queryParameterMap[RPC_NEXT_PAGE] = nextPageKey ?: ""

        return queryParameterMap
    }
}