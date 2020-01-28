package com.tokopedia.sellerhome.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 2020-01-28
 */

data class TooltipModel(
        @SerializedName("title")
        val title: String?,
        @SerializedName("content")
        val content: String?,
        @SerializedName("show")
        val isShow: Boolean = false
)