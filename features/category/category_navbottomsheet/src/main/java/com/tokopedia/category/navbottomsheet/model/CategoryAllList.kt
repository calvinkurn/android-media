package com.tokopedia.category.navbottomsheet.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.category.navbottomsheet.model.CategoriesItem

data class CategoryAllList(

        @field:SerializedName("categories")
        val categories: List<CategoriesItem?>? = null,

        @field:SerializedName("categoryDetailData")
        var categoryDetailData: CategoryDetailData? = null
)