package com.tokopedia.tokopedianow.data

import com.tokopedia.abstraction.common.data.model.response.Header
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse.CategoryListResponse
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse.CategoryListResponse.CategoryResponse

fun createCategoryList(
        categoryList: List<CategoryResponse> = listOf(createCategoryResponse(), createAdultCategoryResponse())
): CategoryListResponse {
    return CategoryListResponse(
        header = Header(),
        data = categoryList
    )
}

fun createCategoryResponse(): CategoryResponse {
        return CategoryResponse(
                id = "1122",
                name = "Category 1",
                url = "www.testing.com",
                appLinks = "tokopedia://testing",
                isAdult = 0,
                color = "#FFFFFF"
        )
}

fun createAdultCategoryResponse(): CategoryResponse {
        return CategoryResponse(
                id = "1123",
                name = "Category 2",
                url = "www.testing.com",
                appLinks = "tokopedia://testing",
                isAdult = 1,
                color = "#FFFFFF"
        )
}
