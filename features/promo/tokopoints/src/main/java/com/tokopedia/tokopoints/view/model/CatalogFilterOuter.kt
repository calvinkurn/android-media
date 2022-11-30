package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CatalogFilterOuter(
    @Expose
    @SerializedName("filter")
    var filter: CatalogFilterBase = CatalogFilterBase()
)
