package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryGridUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryListUiModel

object HomeCategoryMapper {

    private const val MAX_CATEGORY_ITEM_COUNT = 19
    private const val THRESHOLD_CATEGORY_ITEM_COUNT = 6
    private const val GRID_SPAN_COUNT_MORE_THAN_THRESHOLD = 2
    private const val GRID_SPAN_COUNT_DEFAULT = 1
    private const val NOT_ADULT_CATEGORY = 0

    fun mapToCategoryLayout(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val categoryGridUiModel = TokoNowCategoryGridUiModel(
            id = response.id,
            title = response.header.name,
            categoryListUiModel = null,
            state = TokoNowLayoutState.LOADING
        )
        return HomeLayoutItemUiModel(categoryGridUiModel, state)
    }

    fun mapToCategoryList(response: List<CategoryResponse>?, warehouseId: String, headerName: String): TokoNowCategoryListUiModel {
        val newCategoryList = mutableListOf<TokoNowCategoryItemUiModel>()
        val responseCategoryList = response?.take(MAX_CATEGORY_ITEM_COUNT).orEmpty()
        val gridSpanCount = getGridSpanCount(
            size = response?.size.orZero()
        )

        // Add "Semua Kategori" item to the list
        newCategoryList.add(
            TokoNowCategoryItemUiModel(
                warehouseId = warehouseId,
                appLink = ApplinkConstInternalTokopediaNow.CATEGORY_LIST,
            )
        )

        // Map response category items to ui model and add all of them
        newCategoryList.addAll(
            responseCategoryList.filter {
                it.isAdult == NOT_ADULT_CATEGORY
            }.map {
                TokoNowCategoryItemUiModel(
                    id = it.id,
                    title = it.name,
                    imageUrl = it.imageUrl,
                    appLink = it.appLinks,
                    headerName = headerName
                )
            }
        )

        return TokoNowCategoryListUiModel(newCategoryList, gridSpanCount)
    }

    private fun getGridSpanCount(size: Int): Int {
        return if (size > THRESHOLD_CATEGORY_ITEM_COUNT) {
            GRID_SPAN_COUNT_MORE_THAN_THRESHOLD
        } else {
            GRID_SPAN_COUNT_DEFAULT
        }
    }
}