package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.horizontalcategory.CategoryNavigationRepository
import javax.inject.Inject

class CategoryNavigationUseCase @Inject constructor(private val categoryNavigationRepository: CategoryNavigationRepository) {

    suspend fun getCategoryNavigationData(componentId: String, pageIdentifier: String): Boolean {
        val component = getComponent(componentId, pageIdentifier)
        if (component?.noOfPagesLoaded == 1)
            return false
        component?.let { cmp ->
            component.data?.get(0)?.categoryDetailUrl?.let { categoryNavigationRepository.getCategoryNavigationData(it) }?.let { cmp.setComponentsItem(it, component.tabName) }
            cmp.noOfPagesLoaded = 1
            return true
        }
        return false
    }

}