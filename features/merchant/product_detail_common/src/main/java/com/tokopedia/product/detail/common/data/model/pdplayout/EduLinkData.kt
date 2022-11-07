package com.tokopedia.product.detail.common.data.model.pdplayout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created by yovi.putra on 03/10/22"
 * Project name: android-tokopedia-core
 **/

data class EduLinkData(
    @SerializedName("appLink")
    @Expose
    val appLink: String = String.EMPTY,
)
