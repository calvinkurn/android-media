package com.tokopedia.common_category.data.kategorymodel

import com.google.gson.annotations.SerializedName

data class CategoriesItem(

        @field:SerializedName("type")
        var type: Int = 1,

        @field:SerializedName("identifier")
        val identifier: String? = null,

        @field:SerializedName("hexColor")
        val hexColor: String? = null,

        @field:SerializedName("parentName")
        val parentName: String? = null,

        @field:SerializedName("iconImageUrl")
        val iconImageUrl: String? = null,

        @field:SerializedName("applinks")
        val applinks: String? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("iconBannerURL")
        val iconBannerURL: String? = null,

        @field:SerializedName("url")
        val url: String? = null,

        @field:SerializedName("iconImageUrlGray")
        val iconImageUrlGray: String? = null,

        @field:SerializedName("child")
        val child: List<ChildItem?>? = null,

        @field:SerializedName("isSelected")
        var isSelected: Boolean = false,

        @field:SerializedName("position")
        var position: Int = 0


)