package com.tokopedia.discovery2.usecase.productCardCarouselUseCase

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.RefreshType
import com.tokopedia.analytics.byteio.SourcePageType
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant.ChooseAddressQueryParams.RPC_PRODUCT_ID
import com.tokopedia.discovery2.Constant.ChooseAddressQueryParams.RPC_USER_WAREHOUSE_ID
import com.tokopedia.discovery2.DiscoveryAppLogPageState
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.Utils.Companion.addAddressQueryMapWithWareHouse
import com.tokopedia.discovery2.analytics.TrackingMapper.updatePaginatedPosition
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.productcarditem.ProductCardRequest
import com.tokopedia.discovery2.datamapper.discoComponentQuery
import com.tokopedia.discovery2.datamapper.getCartData
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.datamapper.getMapWithoutRpc
import com.tokopedia.discovery2.repository.productcards.ProductCardsRepository
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.CATEGORY_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.EMBED_CATEGORY
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.PIN_PRODUCT
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.PRODUCT_ID
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.productcard.experiments.ProductCardExperiment
import javax.inject.Inject

class ProductCardsUseCase @Inject constructor(
    private val productCardsRepository: ProductCardsRepository,
    private val appLogPageState: DiscoveryAppLogPageState
) {
    companion object {
        const val NO_PRODUCT_PER_PAGE = -1

        const val PRODUCT_PER_PAGE = 20
        private const val RPC_FILTER_KEU = "rpc_"
        private const val PAGE_START = 1
        private const val RPC_PAGE_NUMBER = "rpc_page_number"
        private const val RPC_NEXT_PAGE = "rpc_next_page"
        private const val RPC_PAGE__SIZE = "rpc_page_size"
        private const val RPC_WAREHOUSE_TCO = "rpc_warehouse_tco"
        private const val PARAM_L_NAME = "l_name"
        private const val VALUE_L_NAME_SRE = "sre"
        private const val PARAM_ENTER_FROM = "enterFrom"
        private const val PARAM_SOURCE_PAGE_TYPE = "sourcePageType"
    }

    suspend fun loadFirstPageComponents(
        componentId: String,
        pageEndPoint: String,
        productsLimit: Int = PRODUCT_PER_PAGE
    ): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        val paramWithoutRpc = getMapWithoutRpc(pageEndPoint)
        if (isAlreadyLoaded(component)) return false
        component?.let {
            val parentComponentsItem = getComponent(it.parentComponentId, pageEndPoint)

            val request = ProductCardRequest(
                it.getComponentId(),
                pageEndPoint,
                it.compAdditionalInfo?.tracker?.sessionId.orEmpty(),
                it.name,
                appLogPageState.getLastState()
            )

            val (productListData, additionalInfo) = productCardsRepository.getProducts(
                request,
                getQueryParameterMap(
                    PAGE_START,
                    parentComponentsItem?.chipSelectionData,
                    it.selectedFilters,
                    it.selectedSort,
                    parentComponentsItem?.data,
                    productsLimit,
                    componentId,
                    pageEndPoint,
                    it.nextPageKey,
                    it.recomQueryProdId,
                    paramWithoutRpc,
                    it.userAddressData,
                    component.properties?.warehouseTco
                )
            )
            it.showVerticalLoader = productListData.isNotEmpty()
            it.setComponentsItem(productListData, component.tabName)
            it.noOfPagesLoaded = 1
            it.nextPageKey = additionalInfo?.nextPage
            it.compAdditionalInfo = additionalInfo
            if (productListData.isEmpty()) return true
            if (it.properties?.tokonowATCActive == true) {
                Utils.updateProductAddedInCart(productListData, getCartData(pageEndPoint))
            }
            it.pageLoadedCounter = 2
            it.verticalProductFailState = false
            return true
        }
        return false
    }

    private fun isAlreadyLoaded(component: ComponentsItem?): Boolean {
        val alreadyLoaded =
            if (!component?.parentSectionId.isNullOrEmpty() && component?.isTargetedTabComponent == true) {
                false
            } else {
                component?.noOfPagesLoaded == 1
            }
        return alreadyLoaded
    }

    suspend fun getProductCardsUseCase(
        componentId: String,
        pageEndPoint: String,
        refreshType: RefreshType,
        productsLimit: Int = PRODUCT_PER_PAGE
    ): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        val paramWithoutRpc = getMapWithoutRpc(pageEndPoint)
        val parentComponent = component?.parentComponentId?.let { getComponent(it, pageEndPoint) }
        parentComponent?.let { component1 ->
            val parentComponentsItem = getComponent(component1.parentComponentId, pageEndPoint)

            val request = ProductCardRequest(
                component1.getComponentId(),
                pageEndPoint,
                component1.compAdditionalInfo?.tracker?.sessionId.orEmpty(),
                component1.name,
                refreshType
            )

            val (productListData, additionalInfo) = productCardsRepository.getProducts(
                request,
                getQueryParameterMap(
                    component1.pageLoadedCounter,
                    parentComponentsItem?.chipSelectionData,
                    component1.selectedFilters,
                    component1.selectedSort,
                    parentComponentsItem?.data,
                    productsLimit,
                    componentId,
                    pageEndPoint,
                    component1.nextPageKey,
                    component1.recomQueryProdId,
                    paramWithoutRpc,
                    component.userAddressData,
                    component.properties?.warehouseTco
                )
            )
            component1.nextPageKey = additionalInfo?.nextPage
            if (productListData.isEmpty()) {
                component1.showVerticalLoader = false
            } else {
                component1.pageLoadedCounter += 1
                component1.showVerticalLoader = true
                updatePaginatedData(productListData, component1)
                if (component1.properties?.tokonowATCActive == true) {
                    Utils.updateProductAddedInCart(productListData, getCartData(pageEndPoint))
                }
                (component1.getComponentsItem() as ArrayList<ComponentsItem>).addAll(productListData)
            }
            component1.verticalProductFailState = false
            return true
        }
        return false
    }

    suspend fun getCarouselPaginatedData(
        componentId: String,
        pageEndPoint: String,
        productsLimit: Int = PRODUCT_PER_PAGE
    ): ProductCardPaginationLoadState {
        val component = getComponent(componentId, pageEndPoint)
        val paramWithoutRpc = getMapWithoutRpc(pageEndPoint)
        component?.let {
            it.properties?.let { properties ->
                if (properties.limitProduct && properties.limitNumber.toIntOrZero() == it.getComponentsItem()?.size) {
                    return ProductCardPaginationLoadState.FAILED
                }
            }
            val parentComponentsItem = getComponent(it.parentComponentId, pageEndPoint)

            val request = ProductCardRequest(
                it.getComponentId(),
                pageEndPoint,
                it.compAdditionalInfo?.tracker?.sessionId.orEmpty(),
                it.name,
                RefreshType.LOAD_MORE
            )

            val (productListData, additionalInfo) = productCardsRepository.getProducts(
                request,
                getQueryParameterMap(
                    it.pageLoadedCounter,
                    parentComponentsItem?.chipSelectionData,
                    it.selectedFilters,
                    it.selectedSort,
                    parentComponentsItem?.data,
                    productsLimit,
                    componentId,
                    pageEndPoint,
                    it.nextPageKey,
                    it.recomQueryProdId,
                    paramWithoutRpc,
                    it.userAddressData,
                    component.properties?.warehouseTco
                )
            )
            component.nextPageKey = additionalInfo?.nextPage
            if (productListData.isEmpty() && component.nextPageKey.isNullOrEmpty()) {
                return ProductCardPaginationLoadState.REACH_END_OF_PAGE
            } else {
                it.pageLoadedCounter += 1
            }
            updatePaginatedData(productListData, it)
            if (it.properties?.tokonowATCActive == true) {
                Utils.updateProductAddedInCart(productListData, getCartData(pageEndPoint))
            }
            (it.getComponentsItem() as ArrayList<ComponentsItem>).addAll(productListData)
            return ProductCardPaginationLoadState.LOAD_MORE
        }
        return ProductCardPaginationLoadState.FAILED
    }

    private fun getQueryParameterMap(
        pageNumber: Int,
        chipSelectionData: DataItem?,
        selectedFilters: HashMap<String, String>?,
        selectedSort: HashMap<String, String>?,
        data: List<DataItem>?,
        productsPerPage: Int,
        componentId: String,
        pageEndPoint: String,
        nextPageKey: String?,
        recomProdId: String?,
        queryParameterMapWithoutRpc: Map<String, String>?,
        userAddressData: LocalCacheModel?,
        warehouseTco: String?
    ): MutableMap<String, Any> {
        val queryParameterMap = mutableMapOf<String, Any>()

        queryParameterMap[RPC_PAGE__SIZE] =
            if (productsPerPage == NO_PRODUCT_PER_PAGE) String.EMPTY else productsPerPage
        queryParameterMap[RPC_PAGE_NUMBER] = pageNumber.toString()

        chipSelectionData?.let {
            it.targetComponent?.split(",")?.forEach { targetId ->
                if (componentId == ComponentNames.LoadMore.componentName) {
                    val component = getComponent(componentId, pageEndPoint)
                    if (targetId == component?.parentComponentId) {
                        queryParameterMap[RPC_FILTER_KEU + it.key] = it.value.toString()
                    }
                } else if (targetId == componentId) queryParameterMap[RPC_FILTER_KEU + it.key] =
                    it.value.toString()
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
            val item = data.find { it.isSelected } ?: data.first()
            if (!item.filterKey.isNullOrEmpty() && !item.filterValue.isNullOrEmpty()) {
                queryParameterMap[RPC_FILTER_KEU + item.filterKey] = item.filterValue
            }
        }

        queryParameterMap[RPC_NEXT_PAGE] = nextPageKey ?: ""

        queryParameterMap.putAll(addAddressQueryMapWithWareHouse(userAddressData))
        if (userAddressData?.warehouse_id?.isNotEmpty() == true) {
            queryParameterMap[RPC_USER_WAREHOUSE_ID] = userAddressData.warehouse_id
        }
        if (!recomProdId.isNullOrEmpty()) {
            queryParameterMap[RPC_PRODUCT_ID] = recomProdId
        }
        if (!warehouseTco.isNullOrEmpty()) {
            queryParameterMap[RPC_WAREHOUSE_TCO] = warehouseTco
        }
        queryParameterMapWithoutRpc?.let {
            queryParameterMap.putAll(it)
        }
        if (ProductCardExperiment.isReimagine()) {
        queryParameterMap[PARAM_L_NAME] = VALUE_L_NAME_SRE
        }

        queryParameterMap.appendAppLogFilterParams()

        return queryParameterMap
    }

    private fun MutableMap<String, Any>.appendAppLogFilterParams(): MutableMap<String, Any> {
        val enterFrom = AppLogAnalytics.getLastData(AppLogParam.ENTER_FROM).toString()
        put(PARAM_ENTER_FROM, enterFrom)
        put(PARAM_SOURCE_PAGE_TYPE, SourcePageType.PRODUCT_CARD)

        return this
    }

    private fun updatePaginatedData(
        products: ArrayList<ComponentsItem>,
        parentComponentsItem: ComponentsItem
    ) {
        products.forEach {
            it.apply {
                parentComponentId = parentComponentsItem.id
                pageEndPoint = parentComponentsItem.pageEndPoint
                parentComponentPosition = parentComponentsItem.position
            }
        }

        val offsetPosition = parentComponentsItem.getComponentsItem()?.size.orZero()
        products.updatePaginatedPosition(offsetPosition)
    }

    private fun ComponentsItem.getComponentId(): String {
        val isDynamic = properties?.dynamic ?: false
        return if (isDynamic && !dynamicOriginalId.isNullOrEmpty()) {
            dynamicOriginalId!!
        } else {
            id
        }
    }
}
