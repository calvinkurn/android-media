package com.tokopedia.core.common.category.view.mapper

import com.tokopedia.core.common.category.domain.model.CategoryModel
import com.tokopedia.core.common.category.view.model.CategoryViewModel
import java.util.*

/**
 * @author saidfaisal on 28/7/20.
 */

object CategoryViewMapper {

    fun mapCategoryModelsToCategoryViewModels(categories: List<CategoryModel>): List<CategoryViewModel>? {
        val viewModels: MutableList<CategoryViewModel> = ArrayList()
        for (categoryModel in categories) {
            viewModels.add(getCategoryViewModel(categoryModel))
        }
        return viewModels
    }

    private fun getCategoryViewModel(category: CategoryModel): CategoryViewModel {
        val viewModel = CategoryViewModel()
        viewModel.name = category.name
        viewModel.id = category.id
        viewModel.isHasChild = category.hasChild
        return viewModel
    }

}