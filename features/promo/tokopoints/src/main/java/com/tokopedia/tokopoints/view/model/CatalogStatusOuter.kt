package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CatalogStatusOuter(
    @Expose
    @SerializedName("tokopointsCatalogStatus")
    var catalogStatus: CatalogStatusBase = CatalogStatusBase()
)
