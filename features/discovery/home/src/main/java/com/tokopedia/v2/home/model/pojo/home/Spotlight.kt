package com.tokopedia.v2.home.model.pojo.home

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class Spotlight (
        @SerializedName("spotlights")
        @Expose
        val spotlights: List<SpotlightItem> = ArrayList()
)

data class SpotlightItem(
        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("background_image_url")
        @Expose
        val backgroundImageUrl: String = "",
        @SerializedName("tag_name")
        @Expose
        val tagName: String = "",
        @SerializedName("tag_name_hexcolor")
        @Expose
        val tagNameHexColor: String = "",
        @SerializedName("tag_hexcolor")
        @Expose
        val tagHexColor: String = "",
        @SerializedName("cta_text")
        @Expose
        val ctaText: String = "",
        @SerializedName("cta_text_hexcolor")
        @Expose
        val ctaTextHexColor: String = "",
        @SerializedName("url")
        @Expose
        val url: String = "",
        @SerializedName("applink")
        @Expose
        val applink: String = ""
)