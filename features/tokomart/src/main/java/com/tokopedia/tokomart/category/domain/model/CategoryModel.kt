package com.tokopedia.tokomart.category.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.tokomart.category.domain.model.TokonowCategoryDetail.CategoryDetail
import com.tokopedia.tokomart.searchcategory.domain.model.AceSearchProductModel.SearchProduct

data class CategoryModel(
        @SerializedName("category_detail")
        @Expose
        val categoryDetail: CategoryDetail = CategoryDetail(),

        @SerializedName("ace_search_product_v4")
        @Expose
        val searchProduct: SearchProduct = SearchProduct(),

        @SerializedName("category_filter")
        @Expose
        val categoryFilter: DataValue = DataValue(),

        @SerializedName("quick_filter")
        @Expose
        val quickFilter: DataValue = DataValue(),
)