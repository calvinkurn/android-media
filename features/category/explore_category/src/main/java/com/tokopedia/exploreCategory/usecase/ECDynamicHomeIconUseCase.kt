package com.tokopedia.exploreCategory.usecase

import com.tokopedia.exploreCategory.model.ECDynamicHomeIconData
import com.tokopedia.exploreCategory.repository.ECRepository
import javax.inject.Inject

class ECDynamicHomeIconUseCase @Inject constructor(
        private val repository: ECRepository) {

    suspend fun getHomeIconData(): ECDynamicHomeIconData {
        return repository.getHomeIconData()
    }

}