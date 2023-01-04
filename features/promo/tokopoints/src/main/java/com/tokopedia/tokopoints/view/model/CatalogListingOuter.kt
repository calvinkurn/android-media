package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CatalogListingOuter(
    @Expose @SerializedName("catalog")
    var catalog: CatalogEntity = CatalogEntity()
)
