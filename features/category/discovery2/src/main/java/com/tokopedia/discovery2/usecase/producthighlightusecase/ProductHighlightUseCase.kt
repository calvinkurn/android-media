package com.tokopedia.discovery2.usecase.producthighlightusecase

import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.datamapper.getMapWithoutRpc
import com.tokopedia.discovery2.repository.producthighlightrepository.ProductHighlightRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import javax.inject.Inject

class ProductHighlightUseCase @Inject constructor(private val productHighlightRepository: ProductHighlightRepository) {

    suspend fun loadFirstPageComponents(componentId: String, pageEndPoint: String): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        val paramWithoutRpc = getMapWithoutRpc(pageEndPoint)
        if (component?.noOfPagesLoaded == 1) return false
        component?.let {
            val isDynamic = it.properties?.dynamic ?: false
            val productHighlightData = productHighlightRepository.getProductHighlightData(
                if (isDynamic && !component.dynamicOriginalId.isNullOrEmpty())
                    component.dynamicOriginalId!! else componentId,
                getQueryParameterMap(paramWithoutRpc, it.userAddressData),
                pageEndPoint)
            val productHighlightListData = (productHighlightData?.data
                ?: emptyList()).toMutableList()
            it.noOfPagesLoaded = 1
            it.verticalProductFailState = false
            component.properties = productHighlightData?.properties
            if (productHighlightListData.isEmpty()) return true
            it.data = productHighlightListData
            return true
        }
        return false
    }

    private fun getQueryParameterMap(queryParameterMapWithoutRpc: Map<String, String>?,
                                     userAddressData: LocalCacheModel?): MutableMap<String, Any> {

        val queryParameterMap = mutableMapOf<String, Any>()

        queryParameterMapWithoutRpc?.let {
            queryParameterMap.putAll(it)
        }
        queryParameterMap.putAll(Utils.addAddressQueryMapWithWareHouse(userAddressData))

        return queryParameterMap
    }
}
