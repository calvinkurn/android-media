package com.tokopedia.browse.categoryNavigation.data.model.category

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class CategoriesItem(

        @field:SerializedName("template")
        val template: String? = null,

        @field:SerializedName("isRevamp")
        val isRevamp: Boolean? = null,

        @field:SerializedName("identifier")
        val identifier: String? = null,

        @field:SerializedName("iconImageUrl")
        val iconImageUrl: String? = null,

        @field:SerializedName("applinks")
        val applinks: String? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("isIntermediary")
        val isIntermediary: Boolean? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("isAdult")
        val isAdult: Int? = null,

        @field:SerializedName("url")
        val url: String? = null,

        @field:SerializedName("child")
        var child: List<ChildItem>? = null,

        var isSelected: Boolean = false,
        var isExpanded: Boolean = false,


        var type: Int = 2

)