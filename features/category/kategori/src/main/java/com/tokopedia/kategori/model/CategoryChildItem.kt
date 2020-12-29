package com.tokopedia.kategori.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.common_category.data.kategorymodel.ChildItem

data class CategoryChildItem(

        @field:SerializedName("sameCategoryTotalCount")
        var sameCategoryTotalCount: Int = 0,

        @field:SerializedName("categoryPosition")
        var categoryPosition: Int = 0,

        @field:SerializedName("itemType")
        var itemType: Int? = null,

        @field:SerializedName("isSeringKamuLihat")
        var isSeringKamuLihat: Boolean = false,

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

        @field:SerializedName("child")
        val child: List<ChildItem?>? = null
)