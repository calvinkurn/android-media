package com.tokopedia.tokopoints.view.model.section

import com.google.gson.annotations.SerializedName

data class TextAttributes(
    @SerializedName("text")
    var text: String = "",
    @SerializedName("color")
    var color: String = "",
    @SerializedName("isBold")
    var isBold:Boolean= false,
)
