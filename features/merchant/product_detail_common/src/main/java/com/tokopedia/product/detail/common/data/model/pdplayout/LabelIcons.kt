package com.tokopedia.product.detail.common.data.model.pdplayout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by yovi.putra on 23/01/24"
 * Project name: android-tokopedia-core
 **/

data class LabelIcons(
    @SerializedName("label")
    @Expose
    val label: String = "",
    @SerializedName("iconURL")
    @Expose
    val iconURL: String = "",
    @SerializedName("type")
    @Expose
    val type: String = ""
)
