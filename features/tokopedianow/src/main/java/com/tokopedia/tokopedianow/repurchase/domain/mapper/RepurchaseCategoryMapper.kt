package com.tokopedia.tokopedianow.repurchase.domain.mapper

import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryListUiModel

object RepurchaseCategoryMapper {
    private const val MAX_CATEGORY_ITEM_COUNT = 9
    private const val GRID_SPAN_COUNT_MORE_THAN_SEVEN = 2
    private const val GRID_SPAN_COUNT_DEFAULT = 1

    fun mapToCategoryList(response: List<CategoryResponse>?, warehouseId: String): TokoNowCategoryListUiModel {
        val newCategoryList = mutableListOf<TokoNowCategoryItemUiModel>()
        // set all categories entry point for being the first item of category grid depending on the size of category
        var gridSpanCount: Int = GRID_SPAN_COUNT_DEFAULT
        val responseCategoryList = response?.take(MAX_CATEGORY_ITEM_COUNT).orEmpty()

        when (responseCategoryList.size) {
            MAX_CATEGORY_ITEM_COUNT -> {
                newCategoryList.add(
                    TokoNowCategoryItemUiModel(
                        warehouseId = warehouseId,
                        appLink = ApplinkConstInternalTokopediaNow.CATEGORY_LIST,
                    )
                )
                gridSpanCount = GRID_SPAN_COUNT_MORE_THAN_SEVEN
            }
            MAX_CATEGORY_ITEM_COUNT - 1 -> {
                gridSpanCount = GRID_SPAN_COUNT_MORE_THAN_SEVEN
            }
        }
        // then set category response
        newCategoryList.addAll(
            responseCategoryList.map {
                TokoNowCategoryItemUiModel(
                    id = it.id,
                    title = it.name,
                    imageUrl = it.imageUrl,
                    appLink = it.appLinks
                )
            }
        )

        return TokoNowCategoryListUiModel(newCategoryList, gridSpanCount)
    }
}