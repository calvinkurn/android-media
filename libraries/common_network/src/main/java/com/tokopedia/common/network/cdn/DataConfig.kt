package com.tokopedia.common.network.cdn

import com.google.gson.annotations.SerializedName

data class DataConfig(
    @SerializedName("page_name_list")
    val pageNameList: List<String>?,
    @SerializedName("send_success")
    val sendSuccess: Boolean = false,
    @SerializedName("send_failed")
    val sendFailed: Boolean = false,
    @SerializedName("image_url_list")
    val imageUrlList: List<String>?
)
