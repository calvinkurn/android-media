package com.tokopedia.core.common.category.domain.model

import com.google.gson.annotations.SerializedName

data class CategoriesResponse(
        @SerializedName("categoryAllListLite")
        val categories: Categories =  Categories()
)

data class Categories(
        @SerializedName("categories")
        val categories: List<Category> = listOf()
)

data class Category(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("name")
        val name: String = ""
)