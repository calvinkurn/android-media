package com.tokopedia.discovery2.usecase.bannerusecase

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.banner.BannerRepository
import javax.inject.Inject

class BannerUseCase @Inject constructor(private val repository: BannerRepository) {

    suspend fun loadFirstPageComponents(componentId: String, pageEndPoint: String): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        if (component?.noOfPagesLoaded == 1) return false
        component?.let {
            val isDynamic = it.properties?.dynamic ?: false
            val bannerListData = repository.getBanner(
                    if (isDynamic && !component.dynamicOriginalId.isNullOrEmpty())
                        component.dynamicOriginalId!! else componentId,
                    mutableMapOf(),
                    pageEndPoint, it.name)
            it.noOfPagesLoaded = 1
            it.verticalProductFailState = false
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


    private fun getPlaceHolderImage(listSize: Int, componentId: String, pageEndPoint: String): List<Pair<String?,Float>>? {
        val component = getComponent(componentId, pageEndPoint)
        when (component?.name) {
            ComponentNames.DoubleBanner.componentName -> {
                if (listSize == 1) {
                    return listOf(Pair(DUMMY,1.0f))
                }
            }
            ComponentNames.TripleBanner.componentName -> {
                if (listSize == 1) {
                    return listOf(Pair(DUMMY,1.0f),Pair(DUMMY,1.0f))
                } else if (listSize == 2) {
                    return listOf(Pair("dummy",1.0f))
                }
            }
            ComponentNames.QuadrupleBanner.componentName -> {
                if (listSize == 1) {
                    return listOf(Pair(DUMMY,1.0f),Pair(DUMMY,1.0f),Pair(DUMMY,1.0f))
                } else if (listSize == 2) {
                    return listOf(Pair(DUMMY,1.0f),Pair(DUMMY,1.0f))
                } else if (listSize == 3) {
                    return listOf(Pair(DUMMY,1.0f))
                }
            }
            else -> return null
        }
        return null
    }

    companion object{
        const val DUMMY = "dummy"
    }

}