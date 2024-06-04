package com.tokopedia.product.detail.data.model.dynamiconeliner

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.common.data.model.pdplayout.ImageSize
import com.tokopedia.product.detail.common.data.model.pdplayout.Padding

data class DynamicOneLiner(
    @SerializedName("name")
    @Expose
    val name: String = "",

    @SerializedName("text")
    @Expose
    val text: String = "",

    @SerializedName("applink")
    @Expose
    val applink: String = "",

    @SerializedName("separator")
    @Expose
    val separator: String = "",

    @SerializedName("icon")
    @Expose
    val icon: String = "",

    @SerializedName("status")
    @Expose
    val status: String = "",

    @SerializedName("chevronPos")
    @Expose
    val chevronPos: String = "",

    @SerializedName("padding")
    @Expose
    val padding: Padding = Padding(),

    @SerializedName("imageSize")
    @Expose
    val imageSize: ImageSize = ImageSize()
)
