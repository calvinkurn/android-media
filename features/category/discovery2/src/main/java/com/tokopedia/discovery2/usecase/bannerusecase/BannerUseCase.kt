package com.tokopedia.discovery2.usecase.bannerusecase

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.datamapper.getMapWithoutRpc
import com.tokopedia.discovery2.repository.banner.BannerRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import javax.inject.Inject

class BannerUseCase @Inject constructor(private val repository: BannerRepository) {

    suspend fun loadFirstPageComponents(componentId: String, pageEndPoint: String, isDarkMode: Boolean = false): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        val paramWithoutRpc = getMapWithoutRpc(pageEndPoint)
        if (component?.noOfPagesLoaded == CONST_ONE) return false
        component?.let {
            val isDynamic = it.properties?.dynamic ?: false
            val bannerData = repository.getBanner(
                    if (isDynamic && !component.dynamicOriginalId.isNullOrEmpty())
                        component.dynamicOriginalId!! else componentId,
                    getQueryParameterMap(isDarkMode,paramWithoutRpc,it.userAddressData),
                    pageEndPoint, it.name)
            val bannerListData = (bannerData?.data ?: emptyList()).toMutableList()
            it.noOfPagesLoaded = CONST_ONE
            it.verticalProductFailState = false
            component.properties = bannerData?.properties
            if (bannerListData.isEmpty()) return true
            val placeholderImageData = getPlaceHolderImage(bannerListData.size, componentId, pageEndPoint)
            if (!placeholderImageData.isNullOrEmpty()) {
                placeholderImageData.forEach { dataList ->
                    bannerListData.add(DataItem(imageUrlDynamicMobile = dataList.first, itemWeight = dataList.second))
                }
            }
            it.data = bannerListData
            return true
        }
        return false
    }


    private fun getPlaceHolderImage(listSize: Int, componentId: String, pageEndPoint: String): List<Pair<String?, Float>>? {
        val component = getComponent(componentId, pageEndPoint)
        when (component?.name) {
            ComponentNames.DoubleBanner.componentName -> {
                if (listSize == CONST_ONE) {
                    return listOf(COMP_PAIR)
                }
            }
            ComponentNames.TripleBanner.componentName -> {
                if (listSize == CONST_ONE) {
                    return listOf(COMP_PAIR, COMP_PAIR)
                } else if (listSize == CONST_TWO) {
                    return listOf(COMP_PAIR)
                }
            }
            ComponentNames.QuadrupleBanner.componentName -> {
                if (listSize == CONST_ONE) {
                    return listOf(COMP_PAIR, COMP_PAIR, COMP_PAIR)
                } else if (listSize == CONST_TWO) {
                    return listOf(COMP_PAIR, COMP_PAIR)
                } else if (listSize == CONST_THREE) {
                    return listOf(COMP_PAIR)
                }
            }
            else -> return null
        }
        return null
    }

    private fun getQueryParameterMap(isDarkMode: Boolean,
                                     queryParameterMapWithoutRpc: Map<String, String>?,
                                     userAddressData: LocalCacheModel?): MutableMap<String, Any> {

        val queryParameterMap = mutableMapOf<String, Any>()

        queryParameterMap[Utils.DARK_MODE] = isDarkMode
        queryParameterMapWithoutRpc?.let {
            queryParameterMap.putAll(it)
        }
        queryParameterMap.putAll(Utils.addAddressQueryMapWithWareHouse(userAddressData))

        return queryParameterMap
    }

    companion object {
        const val DUMMY = "dummy"
        const val WEIGHT = 1.0f
        const val CONST_ONE = 1
        const val CONST_TWO = 2
        const val CONST_THREE = 3
        val COMP_PAIR = Pair(DUMMY, WEIGHT)
    }

}
