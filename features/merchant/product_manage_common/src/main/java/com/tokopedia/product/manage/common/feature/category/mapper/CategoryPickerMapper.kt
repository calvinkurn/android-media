package com.tokopedia.product.manage.common.feature.category.mapper

import com.tokopedia.product.manage.common.feature.category.model.CategoriesResponse
import com.tokopedia.product.manage.common.feature.category.model.Category
import com.tokopedia.product.manage.common.feature.category.model.CategoryModel
import com.tokopedia.product.manage.common.feature.category.model.CategorySelectedModel

/**
 * @author saidfaisal on 28/7/20.
 */

object CategoryPickerMapper {

    fun mapCategoryResponseIntoCategoryModels(categoryResponse: CategoriesResponse): List<CategoryModel> {
        val categories = categoryResponse.categories.categories
        return mapCategoriesIntoCategoryModels(categories)
    }

    fun findCategoryChildInCategoriesWithCategoryId(categoryId: Long, categories: List<CategorySelectedModel>): List<CategoryModel> {
        categories.forEachIndexed { _, category ->
            if (categoryId == category.categoryId) {
                return mapCategoriesIntoCategoryModels(category.child)
            }
        }
        return listOf()
    }

    fun mapCategoryResponseToCategorySelectedModels(categoryResponse: CategoriesResponse): List<CategorySelectedModel> {
        val categories = categoryResponse.categories.categories
        val categorySelectedModel = mutableListOf<CategorySelectedModel>()
        return mapCategoriesToCategorySelectedModels(categories, categorySelectedModel)
    }

    private fun mapCategoriesIntoCategoryModels(categories: List<Category>) : List<CategoryModel> {
        val categoryModels = mutableListOf<CategoryModel>()
        categories.forEach { category ->
            val categoryModel = CategoryModel().apply {
                id = category.id.toLong()
                name = category.name
                hasChild = category.child.isNotEmpty()
            }
            categoryModels.add(categoryModel)
        }
        return categoryModels
    }

    private fun mapCategoriesToCategorySelectedModels(categories: List<Category>, categorySelectedModels: MutableList<CategorySelectedModel>): List<CategorySelectedModel> {
        categories.forEach { category ->
            val categoryModel = CategorySelectedModel().apply {
                categoryId = category.id.toLong()
                child = category.child
                mapCategoriesToCategorySelectedModels(child, categorySelectedModels)
            }
            categorySelectedModels.add(categoryModel)
        }
        return categorySelectedModels
    }

}
