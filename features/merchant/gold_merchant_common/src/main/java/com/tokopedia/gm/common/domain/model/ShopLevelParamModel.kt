package com.tokopedia.gm.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by @ilhamsuaib on 19/05/22.
 */

data class ShopLevelParamModel(
    @SerializedName("shopID")
    @Expose
    val shopId: String,
    @SerializedName("source")
    @Expose
    val source: String = "android",
    @SerializedName("lang")
    @Expose
    val language: String = "id"
)