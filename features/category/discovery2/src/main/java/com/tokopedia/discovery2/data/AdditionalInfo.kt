package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName

data class AdditionalInfo(
        @SerializedName("category")
        val category: Category?
)

data class Category(
        @SerializedName("levels")
        val levels: List<Level>?
)

data class Level(
        @SerializedName("id")
        val categoryId: Int?,
        @SerializedName("level")
        val level: Int?,
        @SerializedName("name")
        val name: String?
)