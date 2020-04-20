package com.tokopedia.product.manage.feature.quickedit.variant.data.model

import com.google.gson.annotations.SerializedName

data class Header(
    @SerializedName("messages")
    val messages: List<String>?,
    @SerializedName("reason")
    val reason: String?,
    @SerializedName("errorCode")
    val errorCode: String?
)