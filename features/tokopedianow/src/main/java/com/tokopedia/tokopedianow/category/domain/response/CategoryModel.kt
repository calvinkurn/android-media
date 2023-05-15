package com.tokopedia.tokopedianow.category.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel

data class CategoryModel(
    @SerializedName("ace_search_product_v4")
    @Expose
    val searchProduct: AceSearchProductModel.SearchProduct = AceSearchProductModel.SearchProduct(),
)
