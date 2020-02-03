package com.tokopedia.sellerhome.domain.model

import com.google.gson.annotations.SerializedName

data class TooltipListItemModel(
        @SerializedName("title")
        val title: String?,
        @SerializedName("description")
        val description: String?
)