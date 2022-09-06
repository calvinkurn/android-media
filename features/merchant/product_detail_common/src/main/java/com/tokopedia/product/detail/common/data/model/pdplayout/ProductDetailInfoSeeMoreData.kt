package com.tokopedia.product.detail.common.data.model.pdplayout

import com.google.gson.annotations.SerializedName

/**
 * Created by yovi.putra on 05/09/22"
 * Project name: android-tokopedia-core
 **/

data class ProductDetailInfoSeeMoreData(
    @SerializedName("actionTitle")
    val actionTitle: String = "",
    @SerializedName("param")
    val param: String = ""
)