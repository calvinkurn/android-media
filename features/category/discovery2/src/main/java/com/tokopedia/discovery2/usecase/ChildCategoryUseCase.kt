package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.childcategory.ChildCategoryRepository
import javax.inject.Inject

class ChildCategoryUseCase @Inject constructor(private val childCategoryRepository: ChildCategoryRepository) {
    suspend fun getChildCategory(componentId: String, pageEndPoint: String) : List<ComponentsItem> {
        return getComponent(componentId, pageEndPoint)?.getComponentsItem() ?: childCategoryRepository
                .getChildCategory(componentId = componentId,
                        pageEndPoint = pageEndPoint)
    }
}