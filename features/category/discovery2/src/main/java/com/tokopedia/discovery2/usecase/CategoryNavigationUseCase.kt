package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.repository.horizontalcategory.CategoryNavigationRepository
import javax.inject.Inject

class CategoryNavigationUseCase @Inject constructor(private val categoryNavigationRepository: CategoryNavigationRepository) {

    suspend fun getCategoryNavigationData(categoryDetailUrl: String): ArrayList<ComponentsItem> {
       return categoryNavigationRepository.getCategoryNavigationData(categoryDetailUrl)
    }

}