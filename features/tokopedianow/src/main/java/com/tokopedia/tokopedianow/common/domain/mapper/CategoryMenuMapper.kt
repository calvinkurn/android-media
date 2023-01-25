package com.tokopedia.tokopedianow.common.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemSeeAllUiModel
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemUiModel

object CategoryMenuMapper {
    const val APPLINK_PARAM_WAREHOUSE_ID = "?warehouse_id="

    private const val MAX_CATEGORY_ITEM_COUNT = 9
    private const val NOT_ADULT_CATEGORY = 0

    fun mapToCategoryLayout(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val categoryMenuUiModel = TokoNowCategoryMenuUiModel(
            id = response.id,
            title = response.header.name,
            categoryListUiModel = null,
            state = TokoNowLayoutState.LOADING
        )
        return HomeLayoutItemUiModel(categoryMenuUiModel, state)
    }

    fun mapToCategoryList(
        response: List<GetCategoryListResponse.CategoryListResponse.CategoryResponse>?,
        headerName: String = "",
        seeAllAppLink: String = ""
    ): List<Visitable<*>> {
        val newCategoryList = mutableListOf<Visitable<*>>()
        val responseCategoryList = response?.take(MAX_CATEGORY_ITEM_COUNT).orEmpty()

        // Map response category items to ui model and add all of them
        newCategoryList.addAll(
            responseCategoryList.filter {
                it.isAdult == NOT_ADULT_CATEGORY
            }.map {
                TokoNowCategoryMenuItemUiModel(
                    id = it.id,
                    title = it.name,
                    imageUrl = it.imageUrl,
                    appLink = it.appLinks,
                    headerName = headerName
                )
            }
        )
       newCategoryList.add(TokoNowCategoryMenuItemSeeAllUiModel(seeAllAppLink))
        return newCategoryList
    }
}
