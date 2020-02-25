package com.tokopedia.core.common.category.domain.model

import com.google.gson.annotations.SerializedName

data class CategoriesResponse(
        @SerializedName("getCategoryListLite")
        val categories: Categories =  Categories()
)

data class Categories(
        @SerializedName("categories")
        val categories: Children = Children()
)

data class Children(
        @SerializedName("children")
        val categoriesLevelThree: CategoriesLevelThree = CategoriesLevelThree()
)

data class CategoriesLevelThree(
        @SerializedName("children")
        val children: List<Category> = listOf()
)

data class Category(
        @SerializedName("name")
        val name: String = ""
)