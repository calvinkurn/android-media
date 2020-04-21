package com.tokopedia.layanan_finansial.view.models

import com.google.gson.annotations.SerializedName

data class LayananListItem(
        @SerializedName("")
        var isVisited : Boolean = false,
        @SerializedName("name")
        val name : String? = null,
        @SerializedName("category")
        val categrory: String? = null,
        @SerializedName("cta")
        val cta : String? = null,
        @SerializedName("url")
        val url : String?= null,
        @SerializedName("image_url")
        val iconUrl: String? = null,
        @SerializedName("status")
        val status:String? = null,
        @SerializedName("status_text_color")
        val statusTextColor: String? = null,
        @SerializedName("status_bg_color")
        val statusBackgroundColor : String? = null,
        @SerializedName("desc_1")
        val desc1: String? = null,
        @SerializedName("desc_2")
        val desc2: String? = null,
        @SerializedName("datalayer_status")
        val datalayerStatus : String? = null
)
