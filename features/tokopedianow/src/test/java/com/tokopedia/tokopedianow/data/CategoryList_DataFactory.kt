package com.tokopedia.tokopedianow.data

import com.tokopedia.abstraction.common.data.model.response.Header
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryListResponse
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse

fun createCategoryList(): CategoryListResponse {
    return CategoryListResponse(
            header = Header(),
            data = listOf(
                    CategoryResponse(
                            id = "1122",
                            name = "Category",
                            url = "www.testing.com",
                            appLinks = "tokopedia://testing"
                    )
            )
    )
}