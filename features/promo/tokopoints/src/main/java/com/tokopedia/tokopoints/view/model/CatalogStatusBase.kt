package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CatalogStatusBase (
    @Expose
    @SerializedName("catalogStatus")
    var catalogs: List<CatalogStatusItem>? = null
)