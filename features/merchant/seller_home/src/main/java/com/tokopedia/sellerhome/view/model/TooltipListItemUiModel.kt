package com.tokopedia.sellerhome.view.model

import com.google.gson.annotations.SerializedName

data class TooltipListItemUiModel(
        @SerializedName("title")
        val title: String,
        @SerializedName("description")
        val description: String
)