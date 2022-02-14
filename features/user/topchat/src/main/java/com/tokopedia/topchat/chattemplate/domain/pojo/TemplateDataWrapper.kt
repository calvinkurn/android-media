package com.tokopedia.topchat.chattemplate.domain.pojo

import com.google.gson.annotations.SerializedName
import com.tokopedia.network.data.model.response.Header

data class TemplateDataWrapper<T>(
    @SerializedName("header")
    val header: Header? = null,
    @SerializedName("data")
    val data: T
)