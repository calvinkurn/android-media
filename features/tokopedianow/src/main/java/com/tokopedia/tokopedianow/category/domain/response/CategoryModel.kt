package com.tokopedia.tokopedianow.category.domain.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse

data class CategoryModel(
    @SerializedName("category_detail")
    val categoryDetail: CategoryDetailResponse.CategoryDetail,

    @SerializedName("category_navigation")
    val categoryNavigation: GetCategoryListResponse.CategoryListResponse
)
