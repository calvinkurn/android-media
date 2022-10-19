package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CatalogFilterPointRange(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("text")
    var text: String = "",
    @Expose(serialize = false, deserialize = false)
    var isSelected:Boolean = false
)
