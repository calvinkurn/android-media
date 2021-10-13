package com.tokopedia.tokopedianow.data

import com.tokopedia.abstraction.common.data.model.response.Header
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryListResponse
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse

fun createCategoryList(
        categoryList: List<CategoryResponse> = listOf(createCategoryResponse())
): CategoryListResponse {
    return CategoryListResponse(
            header = Header(),
            data = categoryList
    )
}

fun createCategoryResponse(): CategoryResponse {
        return CategoryResponse(
                id = "1122",
                name = "Category",
                url = "www.testing.com",
                appLinks = "tokopedia://testing"
        )
}