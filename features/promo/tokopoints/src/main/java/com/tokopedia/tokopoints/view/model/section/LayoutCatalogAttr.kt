package com.tokopedia.tokopoints.view.model.section

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity

data class LayoutCatalogAttr(
        @SerializedName("catalogList")
        @Expose
        val catalogList: List<CatalogsValueEntity>? = null,

        @SerializedName("countdownInfo")
        val countdownInfo: CountDownInfo? = null
)