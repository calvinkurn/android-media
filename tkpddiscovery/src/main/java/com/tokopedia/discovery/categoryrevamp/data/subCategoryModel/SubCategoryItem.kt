package com.tokopedia.discovery.categoryrevamp.data.subCategoryModel

import com.google.gson.annotations.SerializedName

data class SubCategoryItem(

        @field:SerializedName("isAdult")
        val isAdult: Int? = null,

        @field:SerializedName("applinks")
        val applinks: String? = null,

        @field:SerializedName("name")
        var name: String? = null,

        @field:SerializedName("id")
        val id: Int? = null,

        @field:SerializedName("redirectionURL")
        val redirectionURL: String? = null,

        @field:SerializedName("appRedirectionURL")
        val appRedirectionURL: String? = null,

        @field:SerializedName("thumbnailImage")
        val thumbnailImage: String? = null,

        @field:SerializedName("url")
        val url: String? = null,

        @field:SerializedName("is_default")
        var is_default: Boolean = false
)