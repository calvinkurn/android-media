package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToCategoryTitle
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToChooseAddress
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToHeaderSpace
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryNavigationMapper.mapToCategoryNavigation
import com.tokopedia.tokopedianow.category.domain.response.CategoryModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryHeaderSpaceUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel

internal object VisitableMapper {
    fun MutableList<Visitable<*>>.addHeaderSpace(space: Int, categoryModel: CategoryModel) {
        add(categoryModel.mapToHeaderSpace(space))
    }

    fun MutableList<Visitable<*>>.addChooseAddress(categoryModel: CategoryModel)  {
        add(categoryModel.mapToChooseAddress())
    }

    fun MutableList<Visitable<*>>.addCategoryTitle(categoryModel: CategoryModel) {
        add(categoryModel.mapToCategoryTitle())
    }

    fun MutableList<Visitable<*>>.addCategoryMenu(categoryModel: CategoryModel, categoryIdL1: String) {
        add(categoryModel.mapToCategoryNavigation(categoryIdL1))
    }
}
