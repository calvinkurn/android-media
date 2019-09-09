package com.tokopedia.discovery.categoryrevamp.data.catalogModel

import com.google.gson.annotations.SerializedName

data class SearchCatalog(

        @field:SerializedName("count")
        val count: Int = 0,

        @field:SerializedName("items")
        val items: List<CatalogItem?> = ArrayList(),

        @field:SerializedName("status")
        val status: String = ""
)