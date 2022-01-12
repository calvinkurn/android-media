package com.tokopedia.tokopedianow.repurchase.domain.mapper

import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryItemUiModel

object RepurchaseCategoryMapper {
    private const val MAX_CATEGORY_ITEM_COUNT = 9

    fun mapToCategoryList(response: List<CategoryResponse>?, warehouseId: String): List<TokoNowCategoryItemUiModel>? {
        val newCategoryList = mutableListOf<TokoNowCategoryItemUiModel>()
        // set all categories entry point for being the first item of category grid
        newCategoryList.add(
            TokoNowCategoryItemUiModel(
                warehouseId = warehouseId,
                appLink = ApplinkConstInternalTokopediaNow.CATEGORY_LIST,
            )
        )
        // then set category response
        newCategoryList.addAll(
            response?.take(MAX_CATEGORY_ITEM_COUNT)?.map {
                TokoNowCategoryItemUiModel(it.id, it.name, it.imageUrl, it.appLinks)
            }.orEmpty()
        )
        return newCategoryList
    }
}