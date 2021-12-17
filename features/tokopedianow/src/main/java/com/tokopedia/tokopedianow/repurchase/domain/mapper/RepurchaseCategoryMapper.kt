package com.tokopedia.tokopedianow.repurchase.domain.mapper

import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryItemUiModel

object RepurchaseCategoryMapper {
    private const val MAX_CATEGORY_ITEM_COUNT = 8

    fun mapToCategoryList(response: List<CategoryResponse>?): List<TokoNowCategoryItemUiModel>? {
        return response?.take(MAX_CATEGORY_ITEM_COUNT)?.map {
            TokoNowCategoryItemUiModel(it.id, it.name, it.imageUrl, it.appLinks)
        }
    }
}