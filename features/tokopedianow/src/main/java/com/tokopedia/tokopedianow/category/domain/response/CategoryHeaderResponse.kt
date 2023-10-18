package com.tokopedia.tokopedianow.category.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse
import com.tokopedia.tokopedianow.common.domain.model.GetTargetedTickerResponse

data class CategoryHeaderResponse(
    @SerializedName("get_targeted_ticker")
    @Expose
    val targetedTicker: GetTargetedTickerResponse,

    @SerializedName("category_detail")
    val categoryDetail: CategoryDetailResponse.CategoryDetail,

    @SerializedName("category_navigation")
    val categoryNavigation: GetCategoryListResponse.CategoryListResponse
)
