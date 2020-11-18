package com.tokopedia.common_category.data.kategorymodel

import com.google.gson.annotations.SerializedName

data class CategoryAllList(

        @field:SerializedName("categories")
        val categories: List<CategoriesItem?>? = null
)