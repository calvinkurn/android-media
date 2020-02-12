package com.tokopedia.discovery2.usecase.tokopointsUseCase

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.repository.tokopoints.TokopointsRepository
import javax.inject.Inject

class TokopointsListDataUseCase @Inject constructor(val tokopointsRepository: TokopointsRepository) {

    suspend fun getTokopointsDataUseCase(componentId: Int, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String): ArrayList<ComponentsItem> {
        return tokopointsRepository.getTokopointsData(componentId, queryParamterMap, pageEndPoint)
    }
}