package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.chipfilter.ChipFilterRepository
import javax.inject.Inject

class ChipFilterUseCase @Inject constructor(private val chipFilterRepository: ChipFilterRepository) {
    suspend fun getChipFilterData(componentId: String, pageEndPoint: String, position : Int, componentName : String?): List<ComponentsItem> {
        return chipFilterRepository.getChipFilterData(componentId, hashMapOf(), pageEndPoint, position, componentName)
    }
}