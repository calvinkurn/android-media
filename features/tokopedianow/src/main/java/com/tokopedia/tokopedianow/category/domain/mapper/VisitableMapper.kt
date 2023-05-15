package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToCategoryTitle
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToChooseAddress
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToHeaderSpace
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryNavigationMapper.mapToCategoryNavigation
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryPageMapper.mapToShowcaseProductCard
import com.tokopedia.tokopedianow.category.domain.response.CategoryHeaderResponse
import com.tokopedia.tokopedianow.category.domain.response.CategoryModel

internal object VisitableMapper {
    fun MutableList<Visitable<*>>.addHeaderSpace(space: Int, headerResponse: CategoryHeaderResponse) {
        add(headerResponse.mapToHeaderSpace(space))
    }

    fun MutableList<Visitable<*>>.addChooseAddress(headerResponse: CategoryHeaderResponse)  {
        add(headerResponse.mapToChooseAddress())
    }

    fun MutableList<Visitable<*>>.addCategoryTitle(headerResponse: CategoryHeaderResponse) {
        add(headerResponse.mapToCategoryTitle())
    }

    fun MutableList<Visitable<*>>.addCategoryNavigation(headerResponse: CategoryHeaderResponse, categoryIdL1: String) {
        add(headerResponse.categoryNavigation.mapToCategoryNavigation(categoryIdL1))
    }

    fun MutableList<Visitable<*>>.addCategoryShowcase(model: CategoryModel) {
        add(model.mapToShowcaseProductCard())
    }
}
