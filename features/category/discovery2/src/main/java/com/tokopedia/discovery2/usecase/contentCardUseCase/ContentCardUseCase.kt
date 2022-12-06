package com.tokopedia.discovery2.usecase.contentCardUseCase

import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.datamapper.getMapWithoutRpc
import com.tokopedia.discovery2.repository.contentCard.ContentCardRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import javax.inject.Inject

class ContentCardUseCase @Inject constructor(private val contentCardRepository: ContentCardRepository) {
    companion object {
        private const val BANNER_PER_PAGE = 20
        private const val PAGE_START = 1
    }

    suspend fun loadFirstPageComponents(
        componentId: String,
        pageEndPoint: String,
        cardLimit: Int = BANNER_PER_PAGE,
        isDarkMode: Boolean = false
    ): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        val paramWithoutRpc = getMapWithoutRpc(pageEndPoint)
        if (component?.noOfPagesLoaded == 1) return false
        component?.let {
            val isDynamic = it.properties?.dynamic ?: false
            val (contentCardListData, nextPage) = contentCardRepository.getContentCards(
                if (isDynamic && !component.dynamicOriginalId.isNullOrEmpty()) {
                    component.dynamicOriginalId!!
                } else {
                    componentId
                },
                getQueryParameterMap(
                    PAGE_START,
                    cardLimit,
                    it.nextPageKey,
                    isDarkMode,
                    paramWithoutRpc,
                    it.userAddressData
                ),
                pageEndPoint,
                it.name
            )
            it.showVerticalLoader = contentCardListData.isNotEmpty()
            it.setComponentsItem(contentCardListData, component.tabName)
            it.noOfPagesLoaded = 1
            it.nextPageKey = nextPage
            if (contentCardListData.isEmpty()) return true
            it.pageLoadedCounter = 2
            it.verticalProductFailState = false
            return true
        }
        return false
    }

    private fun getQueryParameterMap(
        pageNumber: Int,
        bannerPerPage: Int,
        nextPageKey: String?,
        isDarkMode: Boolean,
        queryParameterMapWithoutRpc: Map<String, String>?,
        userAddressData: LocalCacheModel?
    ): MutableMap<String, Any> {

        val queryParameterMap = mutableMapOf<String, Any>()

        queryParameterMap[Utils.RPC_PAGE_SIZE] = bannerPerPage.toString()
        queryParameterMap[Utils.RPC_PAGE_NUMBER] = pageNumber.toString()
        queryParameterMap[Utils.RPC_NEXT_PAGE] = nextPageKey ?: ""
        queryParameterMapWithoutRpc?.let {
            queryParameterMap.putAll(it)
        }
        queryParameterMap.putAll(Utils.addAddressQueryMapWithWareHouse(userAddressData))

        return queryParameterMap
    }
}
