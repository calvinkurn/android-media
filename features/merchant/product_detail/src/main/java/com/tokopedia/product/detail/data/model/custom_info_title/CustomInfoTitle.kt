package com.tokopedia.product.detail.data.model.custom_info_title

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by yovi.putra on 28/11/22"
 * Project name: android-tokopedia-core
 **/

data class CustomInfoTitle(
    @SerializedName("title")
    @Expose
    val title: String = "",

    @SerializedName("status")
    @Expose
    val status: String = "",

    @SerializedName("componentName")
    @Expose
    val componentName: String = ""
)
