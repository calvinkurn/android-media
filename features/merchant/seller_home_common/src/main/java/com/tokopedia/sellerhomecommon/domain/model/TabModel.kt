package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY

data class TabModel(
    @SerializedName("tabTitle")
    val tabTitle: String = String.EMPTY,
    @SerializedName("tabName")
    val tabName: String = String.EMPTY,
    @SerializedName("page")
    val page: String = String.EMPTY,
    @SerializedName("tag")
    val tag: String = String.EMPTY
)
