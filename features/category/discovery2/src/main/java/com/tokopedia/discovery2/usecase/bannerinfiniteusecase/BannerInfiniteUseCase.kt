package com.tokopedia.discovery2.usecase.bannerinfiniteusecase

import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getCartData
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.productcards.ProductCardsRepository
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import javax.inject.Inject

class BannerInfiniteUseCase @Inject constructor(private val productCardsRepository: ProductCardsRepository) {
    companion object {
        private const val PRODUCT_PER_PAGE = 10
        private const val PAGE_START = 1
        private const val RPC_PAGE_NUMBER = "rpc_page_number"
        private const val RPC_NEXT_PAGE = "rpc_next_page"
    }

    suspend fun loadFirstPageComponents(componentId: String, pageEndPoint: String, productsLimit: Int = PRODUCT_PER_PAGE): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        if (component?.noOfPagesLoaded == 1) return false
        component?.let {
            val isDynamic = it.properties?.dynamic ?: false
            val (productListData,nextPage) = productCardsRepository.getProducts(
                    if (isDynamic && !component.dynamicOriginalId.isNullOrEmpty())
                        component.dynamicOriginalId!! else componentId,
                    getQueryParameterMap(PAGE_START,
                            productsLimit,
                            it.nextPageKey),
                    pageEndPoint, it.name)
            it.showVerticalLoader = productListData.isNotEmpty()
            it.setComponentsItem(productListData, component.tabName)
            it.noOfPagesLoaded = 1
            it.nextPageKey = nextPage
            if (productListData.isEmpty()) return true
            if(it.properties?.tokonowATCActive == true)
                Utils.updateProductAddedInCart(productListData, getCartData(pageEndPoint))
            it.pageLoadedCounter = 2
            it.verticalProductFailState = false
            return true
        }
        return false
    }

    suspend fun getProductCardsUseCase(componentId: String, pageEndPoint: String, productsLimit: Int = PRODUCT_PER_PAGE): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        val parentComponent = component?.parentComponentId?.let { getComponent(it, pageEndPoint) }
        parentComponent?.let { component1 ->
            val isDynamic = component1.properties?.dynamic ?: false
            val (productListData,nextPage) = productCardsRepository.getProducts(
                    if (isDynamic && !component1.dynamicOriginalId.isNullOrEmpty())
                        component1.dynamicOriginalId!! else component1.id,
                    getQueryParameterMap(component1.pageLoadedCounter,
                            productsLimit,
                            component1.nextPageKey),
                    pageEndPoint,
                    component1.name)
            component1.nextPageKey = nextPage
            if (productListData.isEmpty()) {
                component1.showVerticalLoader = false
            } else {
                component1.pageLoadedCounter += 1
                component1.showVerticalLoader = true
                updatePaginatedData(productListData,component1)
                if(component1.properties?.tokonowATCActive == true)
                    Utils.updateProductAddedInCart(productListData, getCartData(pageEndPoint))
                (component1.getComponentsItem() as ArrayList<ComponentsItem>).addAll(productListData)
            }
            component1.verticalProductFailState = false
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
            val isDynamic = it.properties?.dynamic ?: false
            val (productListData,nextPage) = productCardsRepository.getProducts(
                    if (isDynamic && !component.dynamicOriginalId.isNullOrEmpty()) component.dynamicOriginalId!! else componentId,
                    getQueryParameterMap(it.pageLoadedCounter,
                            productsLimit,
                            it.nextPageKey),
                    pageEndPoint,
                    it.name)
            component.nextPageKey = nextPage
            if (productListData.isEmpty()) return false else it.pageLoadedCounter += 1
            updatePaginatedData(productListData,it)
            if(it.properties?.tokonowATCActive == true)
                Utils.updateProductAddedInCart(productListData, getCartData(pageEndPoint))
            (it.getComponentsItem() as ArrayList<ComponentsItem>).addAll(productListData)
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

    private fun updatePaginatedData(voucherListData:ArrayList<ComponentsItem>,parentComponentsItem: ComponentsItem){
        voucherListData.forEach {
            it.parentComponentId = parentComponentsItem.id
            it.pageEndPoint = parentComponentsItem.pageEndPoint
            it.parentComponentPosition = parentComponentsItem.position
        }
    }

}