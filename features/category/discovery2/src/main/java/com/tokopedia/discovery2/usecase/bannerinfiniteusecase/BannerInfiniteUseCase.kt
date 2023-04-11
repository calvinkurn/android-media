package com.tokopedia.discovery2.usecase.bannerinfiniteusecase

import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.datamapper.getMapWithoutRpc
import com.tokopedia.discovery2.repository.bannerinfinite.BannerInfiniteRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import javax.inject.Inject

class BannerInfiniteUseCase @Inject constructor(private val bannerInfiniteRepository: BannerInfiniteRepository) {
    companion object {
        private const val BANNER_PER_PAGE = 20
        private const val PAGE_START = 1
    }

    suspend fun loadFirstPageComponents(componentId: String, pageEndPoint: String, bannersLimit: Int = BANNER_PER_PAGE, isDarkMode: Boolean = false): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        val paramWithoutRpc = getMapWithoutRpc(pageEndPoint)
        if (component?.noOfPagesLoaded == 1) return false
        component?.let {
            val isDynamic = it.properties?.dynamic ?: false
            val (bannerListData,nextPage) = bannerInfiniteRepository.getBanners(
                    if (isDynamic && !component.dynamicOriginalId.isNullOrEmpty())
                        component.dynamicOriginalId!! else componentId,
                    getQueryParameterMap(PAGE_START,
                            bannersLimit,
                            it.nextPageKey,isDarkMode,paramWithoutRpc,it.userAddressData),
                    pageEndPoint, it.name)
            it.showVerticalLoader = bannerListData.isNotEmpty()
            it.setComponentsItem(bannerListData, component.tabName)
            it.noOfPagesLoaded = 1
            it.nextPageKey = nextPage
            if (bannerListData.isEmpty()) return true
            it.pageLoadedCounter = 2
            it.verticalProductFailState = false
            return true
        }
        return false
    }

    suspend fun getBannerUseCase(componentId: String, pageEndPoint: String, bannerLimit: Int = BANNER_PER_PAGE, isDarkMode: Boolean = false): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        val paramWithoutRpc = getMapWithoutRpc(pageEndPoint)
        val parentComponent = component?.parentComponentId?.let { getComponent(it, pageEndPoint) }
        parentComponent?.let { component1 ->
            val isDynamic = component1.properties?.dynamic ?: false
            val (bannerListData,nextPage) = bannerInfiniteRepository.getBanners(
                    if (isDynamic && !component1.dynamicOriginalId.isNullOrEmpty())
                        component1.dynamicOriginalId!! else component1.id,
                    getQueryParameterMap(component1.pageLoadedCounter,
                            bannerLimit,
                            component1.nextPageKey,isDarkMode,paramWithoutRpc,component1.userAddressData),
                    pageEndPoint,
                    component1.name)
            component1.nextPageKey = nextPage
            if (bannerListData.isEmpty()) {
                component1.showVerticalLoader = false
            } else {
                component1.pageLoadedCounter += 1
                component1.showVerticalLoader = true
                updatePaginatedData(bannerListData,component1)
                (component1.getComponentsItem() as ArrayList<ComponentsItem>).addAll(bannerListData)
            }
            component1.verticalProductFailState = false
            return true
        }
        return false
    }

    private fun getQueryParameterMap(pageNumber: Int,
                                     bannerPerPage: Int,
                                     nextPageKey: String?,
                                     isDarkMode: Boolean,
                                     queryParameterMapWithoutRpc: Map<String, String>?,
                                     userAddressData: LocalCacheModel?): MutableMap<String, Any> {

        val queryParameterMap = mutableMapOf<String, Any>()

        queryParameterMap[Utils.RPC_PAGE_SIZE] = bannerPerPage.toString()
        queryParameterMap[Utils.RPC_PAGE_NUMBER] = pageNumber.toString()
        queryParameterMap[Utils.RPC_NEXT_PAGE] = nextPageKey ?: ""
        queryParameterMap[Utils.DARK_MODE] = isDarkMode
        queryParameterMapWithoutRpc?.let {
            queryParameterMap.putAll(it)
        }
        queryParameterMap.putAll(Utils.addAddressQueryMapWithWareHouse(userAddressData))

        return queryParameterMap
    }

    private fun updatePaginatedData(bannerListData:ArrayList<ComponentsItem>,parentComponentsItem: ComponentsItem){
        bannerListData.forEach {
            it.parentComponentId = parentComponentsItem.id
            it.pageEndPoint = parentComponentsItem.pageEndPoint
            it.parentComponentPosition = parentComponentsItem.position
        }
    }

}
