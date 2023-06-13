package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CatalogSortType(
    @Expose
    @SerializedName("id")
    private var id: Int = 0,
    @Expose
    @SerializedName("text")
    private val text: String = ""
)
