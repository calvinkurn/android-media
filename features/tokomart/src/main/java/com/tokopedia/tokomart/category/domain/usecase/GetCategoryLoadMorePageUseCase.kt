package com.tokopedia.tokomart.category.domain.usecase

import com.tokopedia.tokomart.category.domain.model.CategoryModel
import com.tokopedia.usecase.coroutines.UseCase

class GetCategoryLoadMorePageUseCase: UseCase<CategoryModel>() {

    override suspend fun executeOnBackground(): CategoryModel {
        return CategoryModel()
    }
}