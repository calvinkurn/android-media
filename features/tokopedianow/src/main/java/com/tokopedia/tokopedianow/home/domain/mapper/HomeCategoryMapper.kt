package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryGridUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryItemUiModel

object HomeCategoryMapper {

    private const val MAX_HOME_CATEGORY_ITEM_COUNT = 9

    fun mapToCategoryLayout(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val categoryGridUiModel = TokoNowCategoryGridUiModel(
            id = response.id,
            title = response.header.name,
            categoryList = emptyList(),
            state = TokoNowLayoutState.LOADING
        )
        return HomeLayoutItemUiModel(categoryGridUiModel, state)
    }

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
            response?.take(MAX_HOME_CATEGORY_ITEM_COUNT)?.map {
                TokoNowCategoryItemUiModel(it.id, it.name, it.imageUrl, it.appLinks)
            }.orEmpty()
        )
        return newCategoryList
    }
}