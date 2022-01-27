package com.tokopedia.discovery2.usecase.bannerusecase

import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.banner.BannerRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import javax.inject.Inject

class BannerUseCase @Inject constructor(private val repository: BannerRepository) {

    suspend fun loadFirstPageComponents(componentId: String, pageEndPoint: String): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        if (component?.noOfPagesLoaded == 1) return false
        component?.let {
            val isDynamic = it.properties?.dynamic ?: false
            val voucherListData = repository.getBanner(
                    if (isDynamic && !component.dynamicOriginalId.isNullOrEmpty())
                        component.dynamicOriginalId!! else componentId,
                    mutableMapOf(),
                    pageEndPoint, it.name)
            it.noOfPagesLoaded = 1
            it.verticalProductFailState = false
            if (voucherListData.isEmpty()) return true
            it.data = voucherListData
            return true
        }
        return false
    }

}