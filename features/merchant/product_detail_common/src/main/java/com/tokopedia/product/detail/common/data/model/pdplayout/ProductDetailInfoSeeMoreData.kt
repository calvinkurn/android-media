package com.tokopedia.product.detail.common.data.model.pdplayout

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created by yovi.putra on 05/09/22"
 * Project name: android-tokopedia-core
 **/

data class ProductDetailInfoSeeMoreData(
    @SerializedName("actionTitle")
    val actionTitle: String = String.EMPTY,
    @SerializedName("param")
    val param: String = String.EMPTY,
    @SerializedName("bottomSheetTitle")
    val bottomSheetTitle: String = String.EMPTY
)