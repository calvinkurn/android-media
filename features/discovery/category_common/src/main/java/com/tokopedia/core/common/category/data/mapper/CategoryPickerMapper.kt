package com.tokopedia.core.common.category.data.mapper

import com.tokopedia.core.common.category.domain.model.CategoriesResponse
import com.tokopedia.core.common.category.domain.model.Category
import com.tokopedia.core.common.category.domain.model.CategoryModel
import com.tokopedia.core.common.category.domain.model.CategorySelectedModel
import kotlinx.coroutines.NonCancellable.children

object CategoryPickerMapper {

    fun mapCategoryResponseToCategoryModels(categoryResponse: CategoriesResponse): List<CategoryModel> {
        val categories = categoryResponse.categories.categories
        return mapCategoriesIntoCategoryModels(categories)
    }

    fun mapCategoriesIntoCategoryModels(categories: List<Category>) : List<CategoryModel> {
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
        return mapCategories(categories, categorySelectedModel)
    }

    fun findCategoryWithCategoryId(categoryId: Long, categorySelectedModels: MutableList<CategorySelectedModel>): List<CategorySelectedModel> {
        categorySelectedModels.forEachIndexed { _, category ->
            if (categoryId == category.categoryId) {
                return mapCategories(category.child, categorySelectedModels)
            }
        }
        return listOf()
    }

    private fun mapCategories(categories: List<Category>, categorySelectedModels: MutableList<CategorySelectedModel>): List<CategorySelectedModel> {
        categories.forEach { category ->
            val categoryModel = CategorySelectedModel().apply {
                categoryId = category.id.toLong()
                child = category.child
                mapCategories(child, categorySelectedModels)
            }
            categorySelectedModels.add(categoryModel)
        }
        return categorySelectedModels
    }

}