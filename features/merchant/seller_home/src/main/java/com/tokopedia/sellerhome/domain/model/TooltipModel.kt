package com.tokopedia.sellerhome.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 2020-01-28
 */

data class TooltipModel(
        @Expose
        @SerializedName("title")
        val title: String?,
        @Expose
        @SerializedName("content")
        val content: String?,
        @Expose
        @SerializedName("list")
        val list: List<TooltipListItemModel>?
)