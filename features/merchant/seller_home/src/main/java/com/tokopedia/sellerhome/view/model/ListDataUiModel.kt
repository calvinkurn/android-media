package com.tokopedia.sellerhome.view.model

import com.google.gson.annotations.SerializedName

data class ListDataUiModel(
        @SerializedName("dataKey")
        val dataKey: String,
        @SerializedName("list")
        val items: List<ListItemUiModel>
)