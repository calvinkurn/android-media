package com.tokopedia.tokopoints.view.model.section

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LayoutCategoryAttr(
    @SerializedName("categoryTokopointsList")
    @Expose
    var categoryTokopointsList: List<CategoryTokopointsList> = listOf()
)
