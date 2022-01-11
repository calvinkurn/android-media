package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.section.SectionRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import javax.inject.Inject

class SectionUseCase @Inject constructor(private val sectionRepository: SectionRepository) {

    suspend fun getChildComponents(componentId: String, pageEndPoint: String):Boolean{
        val component = getComponent(componentId, pageEndPoint)
        if (component?.noOfPagesLoaded == 1) return false
        component?.let {
            val components = sectionRepository.getComponents(pageEndPoint,it.sectionId,getQueryFilterString(
                it.userAddressData
            ))
            it.setComponentsItem(components)
            it.noOfPagesLoaded = 1
            return true
        }
        return false
    }

    private fun getQueryFilterString(userAddressData:LocalCacheModel?):String {
        val queryParameterMap = mutableMapOf<String, Any>()
        queryParameterMap.putAll(Utils.addAddressQueryMapWithWareHouse(userAddressData))
        return Utils.getQueryString(queryParameterMap)
    }
}