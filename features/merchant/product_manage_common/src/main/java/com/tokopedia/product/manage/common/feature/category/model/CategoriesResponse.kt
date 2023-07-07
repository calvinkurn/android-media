package com.tokopedia.product.manage.common.feature.category.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CategoriesResponse(
        @SerializedName("categoryAllListLite")
        @Expose
        val categories: Categories =  Categories()
)

data class Categories(
        @SerializedName("categories")
        @Expose
        val categories: List<Category> = listOf()
)

data class Category(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("children")
        @Expose
        val child: List<Category> = listOf()
)
