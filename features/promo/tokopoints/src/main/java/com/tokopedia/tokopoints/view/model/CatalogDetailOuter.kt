package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.SerializedName

data class CatalogDetailOuter(
    @SerializedName("detail")
    var detail: CatalogsValueEntity = CatalogsValueEntity()
)
