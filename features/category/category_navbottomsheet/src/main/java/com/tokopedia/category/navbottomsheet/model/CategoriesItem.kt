package com.tokopedia.category.navbottomsheet.model

import com.google.gson.annotations.SerializedName


data class CategoriesItem(

        @field:SerializedName("type")
        var type: Int = 1,

        @field:SerializedName("i")
        val iconImageUrl: String? = null,

        @field:SerializedName("n")
        val name: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("ig")
        val iconImageUrlGray: String? = null,

        @field:SerializedName("c")
        val child: List<ChildItem?>? = null,

        @field:SerializedName("isSelected")
        var isSelected: Boolean = false,

        @field:SerializedName("position")
        var position: Int = 0
)