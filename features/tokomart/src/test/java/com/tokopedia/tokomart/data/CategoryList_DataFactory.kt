package com.tokopedia.tokomart.data

import com.tokopedia.abstraction.common.data.model.response.Header
import com.tokopedia.tokomart.categorylist.domain.model.CategoryListResponse
import com.tokopedia.tokomart.categorylist.domain.model.CategoryResponse

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