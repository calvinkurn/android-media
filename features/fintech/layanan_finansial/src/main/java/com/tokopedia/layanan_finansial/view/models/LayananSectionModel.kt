package com.tokopedia.layanan_finansial.view.models

import com.google.gson.annotations.SerializedName

data class LayananSectionModel (
        @SerializedName("title")
        val title: String? = null,
        @SerializedName("subtitle")
        val subtitle: String? =null,
        @SerializedName("background")
        val backgroundColor: String? = null,
        @SerializedName("type")
        val type:String?= null,
        @SerializedName("widget_list")
        val list: List<LayananListItem>? = null

)
