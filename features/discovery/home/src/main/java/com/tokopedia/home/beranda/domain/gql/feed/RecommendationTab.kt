package com.tokopedia.home.beranda.domain.gql.feed

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RecommendationTab(
    @SerializedName("id")
    @Expose
    val id: String = "",
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("image_url")
    @Expose
    val imageUrl: String = ""
)