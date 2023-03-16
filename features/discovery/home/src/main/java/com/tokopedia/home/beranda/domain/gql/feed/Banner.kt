package com.tokopedia.home.beranda.domain.gql.feed

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class Banner(
    @SerializedName("applink")
    val applink: String = "",
    @SerializedName("bu_attribution", alternate = ["buAttribution"])
    val buAttribution: String = "",
    @SerializedName("creative_name", alternate = ["creativeName"])
    val creativeName: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("image_url", alternate = ["imageUrl"])
    val imageUrl: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("target")
    val target: String = "",
    @SerializedName("url")
    val url: String = "",
    @SerializedName("galaxy_attribution", alternate = ["galaxyAttribution"])
    val galaxyAttribution: String = "",
    @SerializedName("persona")
    val persona: String = "",
    @SerializedName("brand_id", alternate = ["brandID"])
    val brandId: String = "",
    @SerializedName("category_persona", alternate = ["categoryPersona"])
    val categoryPersona: String = ""
)
