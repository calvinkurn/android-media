package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.section.SectionRepository
import javax.inject.Inject

class SectionUseCase @Inject constructor(private val sectionRepository: SectionRepository) {

    suspend fun getChildComponents(componentId: String, pageEndPoint: String):Boolean{
        val component = getComponent(componentId, pageEndPoint)
        if (component?.noOfPagesLoaded == 1) return false
        component?.let {
            sectionRepository.getComponents(pageEndPoint,it.sectionId,getQueryParameterMap())
            return true
        }
        return false
    }

    private fun getQueryParameterMap():MutableMap<String, Any> {
        val queryParameterMap = mutableMapOf<String, Any>()
        return queryParameterMap
    }
}