package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 21/05/20
 */

data class TooltipModel(
        @Expose
        @SerializedName("title")
        val title: String?,
        @Expose
        @SerializedName("content")
        val content: String?,
        @Expose
        @SerializedName("show")
        val shouldShow: Boolean = true,
        @Expose
        @SerializedName("list")
        val list: List<TooltipListItemModel>?
)

data class TooltipListItemModel(
        @Expose
        @SerializedName("title")
        val title: String?,
        @Expose
        @SerializedName("description")
        val description: String?
)