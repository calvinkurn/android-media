package com.tokopedia.product.detail.data.model.sdui

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SDUIData(
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("data")
    @Expose
    val data: List<Data> = emptyList()
) {
    data class Data(
        @SerializedName("template")
        @Expose
        val template: String = ""
    )
}
