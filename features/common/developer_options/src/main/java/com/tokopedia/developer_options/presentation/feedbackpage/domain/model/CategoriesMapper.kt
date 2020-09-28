package com.tokopedia.developer_options.presentation.feedbackpage.domain.model

import com.tokopedia.developer_options.presentation.feedbackpage.domain.response.CategoriesResponse
import com.tokopedia.developer_options.presentation.feedbackpage.domain.response.Category
import javax.inject.Inject

class CategoriesMapper @Inject constructor() {

    fun mapData(data: CategoriesResponse) : CategoriesModel {
        return CategoriesModel().apply {
            categories = data?.categories.map(categoriesItemMapper)
        }
    }

    private val categoriesItemMapper: (Category) -> CategoriesItem = {
        CategoriesItem().apply {
            label = it.label
            value = it.value
        }
    }
}