package com.tokopedia.layanan_finansial.view.models

import com.google.gson.annotations.SerializedName

data class LayananListItem(
        @SerializedName("name")
        val name : String? = null,
        @SerializedName("category")
        val categrory: String? = null,
        @SerializedName("cta")
        val cta : String? = null,
        @SerializedName("url")
        val url : String?= null,
        @SerializedName("image_url")
        val icon_url: String? = null,
        @SerializedName("status")
        val status:String? = null,
        @SerializedName("status_text_color")
        val status_text_color: String? = null,
        @SerializedName("status_bg_color")
        val status_background_color : String? = null,
        @SerializedName("desc_1")
        val desc_1: String? = null,
        @SerializedName("desc_2")
        val desc_2: String? = null,
        @SerializedName("datalayer_status")
        val datalayer_status : String? = null
)
