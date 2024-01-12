package com.tokopedia.discovery2.usecase.supportingbrand

import android.annotation.SuppressLint
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.datamapper.getMapWithoutRpc
import com.tokopedia.discovery2.repository.supportingbrand.SupportingBrandRepository
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import javax.inject.Inject

class SupportingBrandUseCase @Inject constructor(private val repository: SupportingBrandRepository) {

    companion object {
        const val BRAND_PER_PAGE = 10
    }

    suspend fun loadPageComponents(
        componentId: String,
        pageEndPoint: String,
        limit: Int = BRAND_PER_PAGE
    ): SupportingBrandLoadState {
        val component = getComponent(componentId, pageEndPoint)
        val paramWithoutRpc = getMapWithoutRpc(pageEndPoint)
        component?.let {
            it.properties?.let { properties ->
                if (properties.limitProduct && properties.limitNumber.toIntOrZero() ==
                    it.getComponentsItem()?.size
                ) {
                    return SupportingBrandLoadState.FAILED
                }
            }
            val isDynamic = it.properties?.dynamic ?: false
            val formattedComponentId =
                if (isDynamic && !component.dynamicOriginalId.isNullOrEmpty()) {
                    component.dynamicOriginalId!!
                } else {
                    componentId
                }
            val (shopCardListData, nextPage) = repository.getData(
                formattedComponentId,
                pageEndPoint,
                getQueryParameterMap(
                    it.pageLoadedCounter,
                    limit,
                    it.nextPageKey,
                    it.userAddressData,
                    it.selectedFilters,
                    it.selectedSort,
                    paramWithoutRpc
                )
            )
            it.setComponentsItem(
                listComponents = it.appendNextPageData(shopCardListData),
                tabName = component.tabName
            )

            it.noOfPagesLoaded = it.pageLoadedCounter
            it.nextPageKey = nextPage
            if (shopCardListData.isEmpty()) return SupportingBrandLoadState.REACH_END_OF_PAGE else it.pageLoadedCounter += 1
            return SupportingBrandLoadState.LOAD_MORE
        }
        return SupportingBrandLoadState.FAILED
    }

    private fun ComponentsItem.appendNextPageData(
        data: List<ComponentsItem>
    ): List<ComponentsItem> {
        val existing = this.getComponentsItem() ?: emptyList()

        return existing + data
    }

    @SuppressLint("PII Data Exposure")
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
        queryParameterMap[Utils.RPC_NEXT_PAGE] = nextPageKey.orEmpty()
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
        queryParameterMap.putAll(Utils.addAddressQueryMapWithWareHouse(userAddressData))
        return queryParameterMap
    }
}
