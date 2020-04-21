package com.tokopedia.sellerhome.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TooltipListItemModel(
        @Expose
        @SerializedName("title")
        val title: String?,
        @Expose
        @SerializedName("description")
        val description: String?
)