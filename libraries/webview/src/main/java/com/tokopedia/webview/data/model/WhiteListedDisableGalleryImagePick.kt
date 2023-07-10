package com.tokopedia.webview.data.model

import com.google.gson.annotations.SerializedName

data class WhiteListedDisableGalleryImagePick(
    @SerializedName("isEnabled")
    val isEnabled: Boolean = false,
    @SerializedName("urls")
    val urls: List<String> = emptyList()
)
