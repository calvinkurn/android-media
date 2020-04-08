package com.tokopedia.kategori.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.kategori.model.CategoriesItem

data class CategoryAllList(

        @field:SerializedName("categories")
        val categories: List<CategoriesItem?>? = null
)