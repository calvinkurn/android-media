package com.tokopedia.core.common.category.data.repository

import com.tokopedia.core.common.category.data.CategoryDataSource
import com.tokopedia.core.common.category.data.FetchCategoryDataSource


class CategoryRepositoryImpl(val categoryDataSource: CategoryDataSource,
                             val fetchCategoryDataSource: FetchCategoryDataSource
) : CategoryRepository {

    override fun checkCategoryAvailable() =  categoryDataSource.checkCategoryAvailable()

    override fun fetchCategoryDisplay(categoryId: Long) = fetchCategoryDataSource.fetchCategoryDisplay(categoryId)

    override fun getCategoryName(categoryId: Long) = categoryDataSource.getCategoryName(categoryId)
}