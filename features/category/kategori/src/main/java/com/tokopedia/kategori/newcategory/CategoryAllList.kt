package com.tokopedia.kategori.newcategory

import com.google.gson.annotations.SerializedName

data class CategoryAllList(

        @field:SerializedName("categories")
        val categories: List<CategoriesItem?>? = null
)