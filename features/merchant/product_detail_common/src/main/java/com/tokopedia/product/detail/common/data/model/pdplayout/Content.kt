package com.tokopedia.product.detail.common.data.model.pdplayout


import com.google.gson.annotations.SerializedName

data class Content(
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("subtitle")
    var subtitle: String = "",
    @SerializedName("applink")
    val applink: String = "",
    @SerializedName("infoLink")
    val infoLink: String = "",

    // For new product info only
    @SerializedName("showAtFront")
    val showAtFront: Boolean = false,
    @SerializedName("isAnnotation")
    val isAnnotation: Boolean = false,
    @SerializedName("showAtBottomsheet")
    val showAtBottomSheet: Boolean = false
)