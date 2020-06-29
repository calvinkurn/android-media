package com.tokopedia.common_category.data.catalogModel

import com.google.gson.annotations.SerializedName
import com.tokopedia.common_category.data.catalogModel.CatalogItem

data class SearchCatalog(

        @field:SerializedName("count")
        val count: Int = 0,

        @field:SerializedName("items")
        val items: List<CatalogItem?> = ArrayList(),

        @field:SerializedName("status")
        val status: String = ""
)