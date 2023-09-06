package com.tokopedia.product.detail.postatc.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PostAtcComponentData(
    @SerializedName("title")
    @Expose
    val title: String = "",

    @SerializedName("subTitle")
    @Expose
    val subtitle: String = "",

    @SerializedName("image")
    @Expose
    val image: String = "",

    @SerializedName("button")
    @Expose
    val button: PostAtcLayout.ProductPostAtcInfo.Button = PostAtcLayout.ProductPostAtcInfo.Button(),

    @SerializedName("queryParam")
    @Expose
    val queryParam: String = "",

    @SerializedName("thematicID")
    @Expose
    val thematicId: String = ""
)
